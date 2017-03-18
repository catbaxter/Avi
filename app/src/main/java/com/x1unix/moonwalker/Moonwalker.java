package com.x1unix.moonwalker;

/**
 * Facade for Moonwalker API
 */
public class Moonwalker {
    private Grabber grabber;
    public static String TAG = "MoonWalker";
    public static String version = "0.3.0";

    public Moonwalker(String referrer) {
        grabber = new Grabber(referrer);
    }

    public void getMovieByKinopoiskId(final String kinopoiskId, final Listener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Get embedded script content
                    String script = grabber.getPlayerScriptByKinopoiskId(kinopoiskId);

                    // Get iframe url
                    String frameUrl = Parser.getFrameUrlFromScript(script);

                    // Get iframe source
                    String iframeHtml = grabber.getResource(frameUrl);

                    // Get player's frame source
                    String playerUrl = Parser.getPlayerUrlFromHtml(iframeHtml);

                    // Create moon session
                    MoonVideo video = MoonVideo
                            .from(playerUrl, grabber)
                            .fetch();

                    listener.onSuccess(video, grabber.getHttpClient());

                    grabber.resetState();
                } catch (Exception ex) {
                    listener.onError(ex);
                }
            }
        }).start();
    }

    public static String getVersion() {
        return Moonwalker.version;
    }
}