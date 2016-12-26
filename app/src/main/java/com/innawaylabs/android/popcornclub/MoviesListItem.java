package com.innawaylabs.android.popcornclub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MoviesListItem {
    private final String POSTER_PATH = "poster_path";
    private final String MOVIE_ID = "id";
    private final String ADULT = "adult";
    private final String TITLE = "title";

    String posterPath;
    int movieId;
    boolean adultRated;
    String title;

    public MoviesListItem(JSONObject movieJson) {
        this.posterPath = movieJson.optString(POSTER_PATH);
        this.movieId = movieJson.optInt(MOVIE_ID);
        this.adultRated = movieJson.optBoolean(ADULT);
        this.title = movieJson.optString(TITLE);
    }

    public String getPosterPath() {
        return posterPath;
    }

    public int getMovieId() {
        return movieId;
    }

    public boolean isAdultRated() {
        return adultRated;
    }

    public String getTitle() {
        return title;
    }

    public static ArrayList<MoviesListItem> createMoviesList(JSONArray moviesListJson) {
        ArrayList<MoviesListItem> moviesList = new ArrayList<>();

        if (moviesListJson != null) {
            for (int i = 0; i < moviesListJson.length(); i++) {
                try {
                    moviesList.add(new MoviesListItem((JSONObject) moviesListJson.get(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return moviesList;
    }
}
