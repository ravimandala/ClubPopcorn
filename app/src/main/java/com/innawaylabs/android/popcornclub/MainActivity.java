package com.innawaylabs.android.popcornclub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
    private final String QUERY_RESULTS = "results";
    private int currentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        movies = new ArrayList<>();
        adapter = new MoviesAdapter(getApplicationContext(), movies);
        currentList = R.id.mi_top_rated_movies;
        fetchTopMovies(getString(R.string.tmdb_top_rated_movies_suffix));
        rvMoviesList.setAdapter(adapter);
        rvMoviesList.setLayoutManager(
                new GridLayoutManager(this,
                        getResources().getInteger(R.integer.movie_list_preview_columns)));
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
                fetchTopMovies(getString(R.string.tmdb_popular_movies_suffix));
                return true;
            case R.id.mi_top_rated_movies:
                currentList = R.id.mi_top_rated_movies;
                movies.clear();
                fetchTopMovies(getString(R.string.tmdb_top_rated_movies_suffix));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fetchTopMovies(String moviesApiSuffix) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.add(getString(R.string.tmdb_key_api_key), getString(R.string.tmdb_api_v3_key));
        client.get(getString(R.string.tmdb_api_v3_url_prefix) + moviesApiSuffix,
                params,
                new TopMoviesResponseHandler());
    }

    private class TopMoviesResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                movies.addAll(
                        MoviesListItem.createMoviesList(
                                response.getJSONArray(QUERY_RESULTS)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Log.d(TAG, "Http request failed with statusCode = " + statusCode + " and error: " + throwable.getMessage());
            super.onFailure(statusCode, headers, responseString, throwable);
        }
    }
}