package com.innawaylabs.android.popcornclub;

import android.content.res.Configuration;
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

    private ArrayList<MoviesListItem> topRatedMovies;
    private ArrayList<MoviesListItem> popularMovies;
    private MoviesAdapter adapter;
    private int currentList;
    private GridLayoutManager layoutManager;
    private EndlessRecyclerViewScrollListener movieListScrollListener;

    AsyncHttpClient client = new AsyncHttpClient();

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager.setSpanCount(getResources().getInteger(R.integer.movie_list_portrait_columns));
        } else {
            layoutManager.setSpanCount(getResources().getInteger(R.integer.movie_list_landscape_columns));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            currentList = R.id.mi_top_rated_movies;
        } else {
            currentList = savedInstanceState.getInt(Constants.CURRENT_LIST);
        }
        popularMovies = new ArrayList<>();
        topRatedMovies = new ArrayList<>();
        adapter = new MoviesAdapter(
                getApplicationContext(),
                (currentList == R.id.mi_top_rated_movies) ? topRatedMovies : popularMovies);
        fetchTopMovies(1);
        rvMoviesList.setAdapter(adapter);

        int numColumns = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ?
                getResources().getInteger(R.integer.movie_list_portrait_columns):
                getResources().getInteger(R.integer.movie_list_landscape_columns);
        layoutManager = new GridLayoutManager(this, numColumns);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.CURRENT_LIST, currentList);
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
            case R.id.mi_top_rated_movies:
                currentList = item.getItemId();
                // Simply switch the lists and do not load more items yet.
                adapter.setMoviesList(
                        (item.getItemId() == R.id.mi_popular_movies) ? popularMovies : topRatedMovies);
                if (adapter.getMoviesList().size() == 0)
                    fetchTopMovies(1);
                movieListScrollListener.resetState();
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fetchTopMovies(int page) {
        RequestParams params = new RequestParams();
        params.add(getString(R.string.tmdb_key_api_key), getString(R.string.tmdb_api_v3_key));
        params.add(getString(R.string.tmdb_key_page_key), String.valueOf(page));

        String moviesApiSuffix =
                (currentList == R.id.mi_top_rated_movies) ?
                        getString(R.string.tmdb_top_rated_movies_suffix) :
                        getString(R.string.tmdb_popular_movies_suffix);
        client.get(getString(R.string.tmdb_api_v3_url_prefix) + moviesApiSuffix,
                params,
                new TopMoviesResponseHandler());
    }

    private class TopMoviesResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                adapter.getMoviesList().addAll(
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