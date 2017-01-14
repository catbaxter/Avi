package com.x1unix.avi.adapter;


import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.bumptech.glide.Glide;
import com.kinopoisk.Constants;
import com.x1unix.avi.R;
import com.x1unix.avi.model.KPMovie;
import com.x1unix.avi.model.KPMovieItem;

public class CachedMoviesListAdapter extends RecyclerView.Adapter<CachedMoviesListAdapter.MovieViewHolder> {

    private ArrayList<KPMovie> movies;
    private int rowLayout;
    private Context context;
    private String currentLang = "ru";
    private MovieViewHolder movieViewHolder;


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        LinearLayout moviesLayout;
        TextView movieTitle;
        TextView data;
        TextView movieDescription;
        TextView rating;
        ImageView posterView;
        String kpId;
        Context context;


        public MovieViewHolder(View v) {
            super(v);
            moviesLayout = (LinearLayout) v.findViewById(R.id.movies_layout);
            movieTitle = (TextView) v.findViewById(R.id.title);
            data = (TextView) v.findViewById(R.id.subtitle);
            movieDescription = (TextView) v.findViewById(R.id.description);
            rating = (TextView) v.findViewById(R.id.rating);
            posterView = (ImageView) v.findViewById(R.id.poster_preview);
            context = v.getContext();
        }

        public void loadPoster(Context context) {
            Glide.with(context)
                    .load(Constants.getPosterUrl(kpId))
                    .placeholder(R.drawable.no_poster)
                    .into(posterView);
        }
    }

    public CachedMoviesListAdapter(ArrayList<KPMovie> movies, int rowLayout, Context context, Locale currentLocale) {
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.context = context;
        this.currentLang = currentLocale.getLanguage();
    }

    @Override
    public CachedMoviesListAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        KPMovie cMovie = movies.get(position);
        holder.movieTitle.setText(cMovie.getLocalizedTitle(currentLang));
        holder.data.setText(cMovie.getYear());
        holder.movieDescription.setText(cMovie.getShortDescription());
        holder.rating.setText(String.valueOf(cMovie.getStars()));
        holder.kpId = cMovie.getId();
        holder.loadPoster(context);
    }

    @Override
    public int getItemCount() {
        return (movies == null) ? 0 : movies.size();
    }
}