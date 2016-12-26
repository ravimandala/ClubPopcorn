package com.innawaylabs.android.popcornclub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private final Context context;
    private final List<MoviesListItem> moviesList;

    public MoviesAdapter(Context context, List<MoviesListItem> moviesList) {
        this.context = context;
        this.moviesList = moviesList;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);

        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MoviesListItem moviesListItem = moviesList.get(position);

        Picasso.with(getContext())
                .load(getFullPosterPath(moviesListItem.getPosterPath()))
                .resize(246, 328)
                .into(holder.ivMoviePoster);
    }

    private String getFullPosterPath(String posterPath) {
        return context.getString(R.string.tmdb_poster_path_url_prefix) + posterPath;
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMoviePoster;

        public ViewHolder(View itemView) {
            super(itemView);

            ivMoviePoster = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
        }
    }
}
