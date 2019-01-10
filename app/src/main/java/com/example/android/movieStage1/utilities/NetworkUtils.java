package com.example.android.movieStage1.utilities;

import android.net.Uri;

import com.example.android.moviestage2.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {
//https://api.themoviedb.org/3/movie/332562/reviews?api_key=4f50ec040ef617ebb4173894cfdf41ca
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String QUERY_API = "api_key";
    //For YouTube
    private static final String BASE_URL_YOUTUBE = "https://www.youtube.com/watch/";
    private static final String QUERY_YOU_TUBE = "v";

    private static final String API = BuildConfig.API_KEY;


    // The highest rated movies
    private static final String PATH_TOP_RATED = "top_rated";
    // Most Popular
    private static final String PATH_POPULAR = "popular";

    //Reviews
    private final static String PATH_REVIEWS = "reviews";

    //Trialers
    private final static String PATH_VIDEOS = "videos";


    public static URL buildURLForPopularMovies() {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(PATH_POPULAR)
                .appendQueryParameter(QUERY_API, API)
                .build();

        URL getUrl = null;
        try {
            getUrl = new URL(String.valueOf(uri));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return getUrl;
    }

    public static URL buildURLForMostRated() {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(PATH_TOP_RATED)
                .appendQueryParameter(QUERY_API, API)
                .build();

        URL getUrl = null;
        try {
            getUrl = new URL(String.valueOf(uri));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return getUrl;
    }

    public static URL buildURLForTrials(int id) {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(String.valueOf(id))
                .appendPath(PATH_VIDEOS)
                .appendQueryParameter(QUERY_API, API)
                .build();

        URL getUrl = null;
        try {
            getUrl = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return getUrl;
    }
    public static URL buildURLForReviews(int id)  {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(String.valueOf(id))
                .appendPath(PATH_REVIEWS)
                .appendQueryParameter(QUERY_API, API)
                .build();

        URL getUrl;
        try {
            getUrl = new URL(uri.toString());
            return getUrl ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null ;
    }

    public static Uri openTrialOnYouTube (String key) {

        return Uri.parse(BASE_URL_YOUTUBE).buildUpon()
                .appendQueryParameter(QUERY_YOU_TUBE,key)
                .build();
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasNext = scanner.hasNext();
            if (hasNext) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


}
