package com.innawaylabs.android.popcornclub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MoviesListItem {
    private final String POSTER_PATH = "poster_path";
    private final String MOVIE_ID = "id";
    private final String ADULT = "adult";

    String posterPath;
    int movieId;
    boolean adultRated;

    public MoviesListItem(JSONObject movieJson) {
        this.posterPath = movieJson.optString(POSTER_PATH);
        this.movieId = movieJson.optInt(MOVIE_ID);
        this.adultRated = movieJson.optBoolean(ADULT);
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
