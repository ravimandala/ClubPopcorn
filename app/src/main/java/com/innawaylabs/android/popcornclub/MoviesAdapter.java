package com.innawaylabs.android.popcornclub;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.innawaylabs.android.popcornclub.utils.Constants;
import com.innawaylabs.android.popcornclub.utils.MovieUtils;
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
        final MoviesListItem moviesListItem = moviesList.get(position);

        Picasso.with(getContext())
                .load(MovieUtils.getFullPosterPath(getContext(), moviesListItem.getPosterPath()))
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error_placeholder)
                .resize(246, 328)
                .into(holder.ivMoviePoster);
        holder.ivMoviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra(Constants.INTENT_MOVIE_ID, moviesListItem.getMovieId());
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMoviePoster;

        public ViewHolder(final View itemView) {
            super(itemView);

            ivMoviePoster = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
        }
    }
}
