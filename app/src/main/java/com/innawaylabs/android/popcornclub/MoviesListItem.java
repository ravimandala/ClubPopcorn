package com.innawaylabs.android.popcornclub;

import com.innawaylabs.android.popcornclub.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MoviesListItem {
    String posterPath;
    int movieId;
    boolean adultRated;
    String title;

    public MoviesListItem(JSONObject movieJson) {
        this.posterPath = movieJson.optString(Constants.POSTER_PATH);
        this.movieId = movieJson.optInt(Constants.MOVIE_ID);
        this.adultRated = movieJson.optBoolean(Constants.ADULT);
        this.title = movieJson.optString(Constants.TITLE);
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
