package com.innawaylabs.android.popcornclub;

import android.os.Parcel;
import android.os.Parcelable;

import com.innawaylabs.android.popcornclub.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Movie implements Parcelable {
    String posterPath;
    int movieId;
    boolean adultRated;
    String title;

    public Movie(JSONObject movieJson) {
        this.posterPath = movieJson.optString(Constants.POSTER_PATH);
        this.movieId = movieJson.optInt(Constants.MOVIE_ID);
        this.adultRated = movieJson.optBoolean(Constants.ADULT);
        this.title = movieJson.optString(Constants.TITLE);
    }

    protected Movie(Parcel in) {
        posterPath = in.readString();
        movieId = in.readInt();
        adultRated = in.readByte() != 0;
        title = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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

    public static ArrayList<Movie> createMoviesList(JSONArray moviesListJson) {
        ArrayList<Movie> moviesList = new ArrayList<>();

        if (moviesListJson != null) {
            for (int i = 0; i < moviesListJson.length(); i++) {
                try {
                    moviesList.add(new Movie((JSONObject) moviesListJson.get(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return moviesList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeInt(movieId);
        dest.writeByte((byte) (adultRated ? 1 : 0));
        dest.writeString(title);
    }
}
