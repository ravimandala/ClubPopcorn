package com.innawaylabs.android.popcornclub.utils;

import android.content.Context;

import com.innawaylabs.android.popcornclub.R;

public class MovieUtils {
    public static String getFullPosterPath(Context context, String posterPath) {
        return context.getString(R.string.tmdb_poster_path_url_prefix) + posterPath;
    }
}
