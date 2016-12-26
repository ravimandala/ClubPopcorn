package com.innawaylabs.android.popcornclub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.innawaylabs.android.popcornclub.utils.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "POPCORN";

    @BindView(R.id.rv_movies_list)
    RecyclerView rvMoviesList;

    private ArrayList<MoviesListItem> movies;
    private MoviesAdapter adapter;
    private int currentList;
    private GridLayoutManager layoutManager;
    private EndlessRecyclerViewScrollListener movieListScrollListener;

    AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        movies = new ArrayList<>();
        adapter = new MoviesAdapter(getApplicationContext(), movies);
        currentList = R.id.mi_top_rated_movies;
        fetchTopMovies(1);
        rvMoviesList.setAdapter(adapter);
        layoutManager = new GridLayoutManager(this,getResources().getInteger(R.integer.movie_list_preview_columns));
        rvMoviesList.setLayoutManager(layoutManager);
        movieListScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                fetchTopMovies(page + 1);
            }
        };
        rvMoviesList.addOnScrollListener(movieListScrollListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == currentList)
            return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.mi_popular_movies:
                currentList = R.id.mi_popular_movies;
                movies.clear();
                adapter.notifyDataSetChanged();
                movieListScrollListener.resetState();
                fetchTopMovies(1);
                return true;
            case R.id.mi_top_rated_movies:
                currentList = R.id.mi_top_rated_movies;
                fetchTopMovies(1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fetchTopMovies(int page) {
        RequestParams params = new RequestParams();
        params.add(getString(R.string.tmdb_key_api_key), getString(R.string.tmdb_api_v3_key));
        params.add(getString(R.string.tmdb_key_page_key), String.valueOf(page));

        String moviesApiSuffix = (currentList == R.id.mi_top_rated_movies) ? getString(R.string.tmdb_top_rated_movies_suffix) : getString(R.string.tmdb_popular_movies_suffix);
        client.get(getString(R.string.tmdb_api_v3_url_prefix) + moviesApiSuffix,
                params,
                new TopMoviesResponseHandler(page == 1));
    }

    private class TopMoviesResponseHandler extends JsonHttpResponseHandler {
        private boolean clearAll;

        public TopMoviesResponseHandler(boolean clearAll) {
                this.clearAll = clearAll;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                if (clearAll) {
                    movieListScrollListener.resetState();
                    movies.clear();
                }
                movies.addAll(
                        MoviesListItem.createMoviesList(
                                response.getJSONArray(Constants.QUERY_RESULTS)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.d(TAG, "Http request failed with statusCode = " + statusCode + " and error: " + throwable.getMessage());
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Log.d(TAG, "Http request failed with statusCode = " + statusCode + " and error: " + throwable.getMessage());
            super.onFailure(statusCode, headers, responseString, throwable);
        }
    }
}