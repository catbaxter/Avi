package com.x1unix.avi.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.x1unix.avi.ClickListener;
import com.x1unix.avi.MovieDetailsActivity;
import com.x1unix.avi.R;
import com.x1unix.avi.RecyclerTouchListener;
import com.x1unix.avi.adapter.CachedMoviesListAdapter;
import com.x1unix.avi.model.KPMovie;
import com.x1unix.avi.storage.MoviesRepository;
import java.util.ArrayList;

public class DashboardTabFragment extends Fragment {
    protected MoviesRepository moviesRepository;
    protected View noItemsView;
    protected ArrayList<KPMovie> items;
    protected RecyclerView itemsListView;
    protected String currentLang = "ru";
    protected GridLayoutManager gridLayoutManager;
    protected CachedMoviesListAdapter moviesListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (moviesRepository != null) {
            items = getContentItems();
        }

        currentLang = getResources().getConfiguration().locale.getLanguage();

    }

    protected void onLayoutUpdate() {
        updateSpanCount();
    }

    protected int getTabView() {
        return 0;
    }

    protected ArrayList<KPMovie> getContentItems() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getTabView(), container, false);

        noItemsView = view.findViewById(R.id.no_items_block);
        itemsListView = (RecyclerView) view.findViewById(R.id.items_recycler_view);

        initRecycleView();
        renderMovies();

        return view;
    }


    protected void renderMovies() {
        boolean hasItems = (items.size() > 0);

        noItemsView.setVisibility(hasItems ? View.GONE : View.VISIBLE);
        itemsListView.setVisibility(hasItems ? View.VISIBLE : View.GONE);
        moviesListAdapter = new CachedMoviesListAdapter(items,
                R.layout.list_item_movie,
                getContext(),
                getResources().getConfiguration().locale);

        itemsListView.setAdapter(moviesListAdapter);

    }

    protected void configureRecycleView() {
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        int colsCount = (isTablet) ? getResources().getInteger(R.integer.colsCount) : 1;

        gridLayoutManager = new GridLayoutManager(getContext(), colsCount);
        itemsListView.setLayoutManager(gridLayoutManager);
    }

    protected void updateSpanCount() {
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        int colsCount = (isTablet) ? getResources().getInteger(R.integer.colsCount) : 1;
        gridLayoutManager.setSpanCount(colsCount);
    }

    protected void initRecycleView() {
        configureRecycleView();

        // Register RecyclerView event listener
        itemsListView.addOnItemTouchListener(
                new RecyclerTouchListener(getContext(), itemsListView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        openMovie(items.get(position));
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
    }

    protected void openMovie(KPMovie movie) {
        Intent mIntent = new Intent(getContext(), MovieDetailsActivity.class);

        // Put id and title
        mIntent.putExtra("movieId", movie.getId());
        mIntent.putExtra("movieTitle", movie.getLocalizedTitle(currentLang));
        mIntent.putExtra("movieGenre", movie.getGenre());
        mIntent.putExtra("movieRating", movie.getStars());
        mIntent.putExtra("movieDescription", movie.getShortDescription());

        startActivity(mIntent);
    }

    protected DashboardTabFragment setMoviesRepository(MoviesRepository m) {
        moviesRepository = m;
        return this;
    }

    public static DashboardTabFragment getInstance(MoviesRepository m, int iviewId) {
        return (new DashboardTabFragment())
                    .setMoviesRepository(m);
    }

    public void rescanElements() {
        if (moviesRepository != null) {
            items = getContentItems();
        }

        if (moviesListAdapter != null) {
            moviesListAdapter.notifyDataSetChanged();
        }

        renderMovies();
    }

    public void updateLayout() {
        onLayoutUpdate();
    }
}
