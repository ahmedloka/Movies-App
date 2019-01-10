package com.example.android.movieStage1.utilities;


import com.example.android.movieStage1.database.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class JsonUtils {

    private static final String KEY_RESULTS = "results";
    //For popular and mostRated
    private static final String KEY_MOVIE_ID = "id";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_TITLE = "title";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_OVER_VIEW = "overview";
    private static final String KEY_RELEASE_DATE = "release_date";

    //For videos
    private static final String KEY_VIDEO_KEY = "key";
    private static final String KEY_CONTENT = "content";

    private static final String POSTER_URL = "http://image.tmdb.org/t/p/w185";


    public static Movie parseMovieJsonStageOne(String json) throws JSONException {
        if (json.isEmpty()) {
            return null;
        }


        JSONObject jsonObject = new JSONObject(json);

        JSONArray jsonArray;
        jsonArray = jsonObject.getJSONArray(KEY_RESULTS);

        List<String> movieIDList = new ArrayList<>(jsonArray.length());
        List<String> rateList = new ArrayList<>(jsonArray.length());
        List<String> titleList = new ArrayList<>(jsonArray.length());
        List<String> posterList = new ArrayList<>(jsonArray.length());
        List<String> overViewList = new ArrayList<>(jsonArray.length());
        List<String> releaseDataList = new ArrayList<>(jsonArray.length());


        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject data = jsonArray.getJSONObject(i);

            String movieID = data.getString(KEY_MOVIE_ID);
            movieIDList.add(movieID);
            String voteAverage = data.getString(KEY_VOTE_AVERAGE);
            rateList.add(voteAverage);
            String title = data.getString(KEY_TITLE);
            titleList.add(title);
            String posterPath = data.getString(KEY_POSTER_PATH);
            posterList.add(POSTER_URL + posterPath);
            String overView = data.getString(KEY_OVER_VIEW);
            overViewList.add(overView);
            String releaseDate = data.getString(KEY_RELEASE_DATE);
            releaseDataList.add(releaseDate);

        }
        final Movie movie = new Movie(movieIDList, rateList, titleList, posterList, overViewList, releaseDataList);

        movie.setMovieID(movieIDList);
        movie.setVoteAverage(rateList);
        movie.setTitle(titleList);
        movie.setPositionPath(posterList);
        movie.setOverView(overViewList);
        movie.setReleaseData(releaseDataList);


        return movie;

    }

    public static Movie parseMovieJsonReviewStageTwo(String response) {
        Movie movie = new Movie();

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray responseArray = jsonObject.getJSONArray(KEY_RESULTS);


            for (int i = 0; i < response.length(); i++) {
                JSONObject getContent = responseArray.getJSONObject(i);

                String content = getContent.getString(KEY_CONTENT);
                movie.setContent(content);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movie;
    }

    public static Movie parseMovieJsonVideoStageTwo(String json) {
        Movie movie = new Movie();
        try {
            JSONObject response = new JSONObject(json);
            JSONArray jsonArray = response.getJSONArray(KEY_RESULTS);

            List<String> movieKeys = new ArrayList<>(jsonArray.length()) ;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String videoKey = jsonObject.getString(KEY_VIDEO_KEY);
                movieKeys.add(videoKey);
            }

            movie.setKey(movieKeys);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movie;
    }

}
