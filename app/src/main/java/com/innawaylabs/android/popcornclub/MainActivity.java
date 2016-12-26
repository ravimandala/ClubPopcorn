package com.innawaylabs.android.popcornclub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_movies_list)
    RecyclerView rvMoviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        if (rvMoviesList == null) {
            Toast.makeText(this, "Failed to bind view", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Successfully bound the view in MainActivity", Toast.LENGTH_SHORT).show();
        }
    }
}
