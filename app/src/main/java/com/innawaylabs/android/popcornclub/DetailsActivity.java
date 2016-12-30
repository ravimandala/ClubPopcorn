package com.innawaylabs.android.popcornclub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.innawaylabs.android.popcornclub.utils.Constants;
import com.innawaylabs.android.popcornclub.utils.MovieUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailsActivity extends AppCompatActivity {
    private final String TAG = "MovieDetails";

    private int movieId;
    OkHttpClient client = new OkHttpClient();

    @BindView(R.id.tv_movie_title)
    TextView tvMovieTitle;

    @BindView(R.id.tv_overview)
    TextView tvOverview;

    @BindView(R.id.iv_movie_poster)
    ImageView ivMoviePoster;

    @BindView(R.id.tv_release_year)
    TextView tvReleaseYear;

    @BindView(R.id.tv_movie_rating)
    TextView tvMovieRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        // Enabling the Up action button on details activity
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.movieId = getIntent().getIntExtra(Constants.INTENT_MOVIE_ID, -1);
        if (movieId != -1)
            fetchMoviesDetails();
    }

    // Send out the network request
    private void fetchMoviesDetails() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(getString(R.string.tmdb_api_v3_url_prefix)).newBuilder();
        urlBuilder.addPathSegment(String.valueOf(movieId));
        urlBuilder.addQueryParameter(getString(R.string.tmdb_key_api_key), getString(R.string.tmdb_api_v3_key));

        Request request = new Request.Builder().url(urlBuilder.build()).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "API request failed: " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    try {
                        final JSONObject jsonResult = new JSONObject(result);
                        DetailsActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvMovieTitle.setText(jsonResult.optString(Constants.TITLE));
                                Picasso.with(getApplicationContext())
                                        .load(MovieUtils.getFullPosterPath(getApplicationContext(),
                                                jsonResult.optString(Constants.POSTER_PATH)))
                                        .resize(246, 348)
                                        .into(ivMoviePoster);
                                tvReleaseYear.setText(jsonResult.optString(Constants.RELEASE_DATE));
                                tvMovieRating.setText(jsonResult.optString(Constants.VOTE_AVERAGE));
                                tvOverview.setText(jsonResult.optString(Constants.OVERVIEW));
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.w(TAG, "Got an unsuccessful response: " + response);
                }
            }
        });
    }
}
