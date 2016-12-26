package com.innawaylabs.android.popcornclub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_movies_list)
    RecyclerView rvMoviesList;

    private ArrayList<MoviesListItem> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        try {
            movies = MoviesListItem.createMoviesList(
                    new JSONArray(getString(R.string.typical_api_response)));
        } catch (JSONException e) {
            e.printStackTrace();
            movies = new ArrayList<>();
        }
        rvMoviesList.setAdapter(
                new MoviesAdapter(getApplicationContext(), movies));
        rvMoviesList.setLayoutManager(
                new GridLayoutManager(this,
                        getResources().getInteger(R.integer.movie_list_preview_columns)));
    }
}
