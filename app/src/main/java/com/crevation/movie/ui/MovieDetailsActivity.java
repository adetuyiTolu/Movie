package com.crevation.movie.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crevation.movie.AppResource;
import com.crevation.movie.MovieApp;
import com.crevation.movie.R;
import com.crevation.movie.data.model.Movie;
import com.crevation.movie.data.model.Review;
import com.crevation.movie.data.rest.MovieService;
import com.crevation.movie.data.rest.ReviewResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {
    Movie mMovie;

    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.date)
    TextView mDate;
    @InjectView(R.id.content)
    TextView mContent;
    @InjectView(R.id.mov_image)
    ImageView mImageView;
    @InjectView(R.id.toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    MovieService mMovieService;
    ArrayList<Review> mReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getBundles(savedInstanceState);
        init();
        fetchReviews();
    }

    void getBundles(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(AppResource.BUNDLE_STATE);
        } else {
            mMovie = getIntent().getParcelableExtra(AppResource.BUNDLE_DETAILS);
        }
    }

    void init() {

        //initialize View
        mTitle.setText(mMovie.getTitle());
        mDate.setText(mMovie.getReleaseDate());
        mContent.setText(mMovie.getOverview());
        setTitle("Rated " + mMovie.getRatings() + " out of 10");
        Picasso.with(this).load(AppResource.IMAGE_PATH + mMovie.getPosterUrl())
                .placeholder(R.drawable.movie)
                .error(R.drawable.movie).into(mImageView);

        //initialize models
        mMovieService = MovieApp.getMovieService();
    }


    void fetchReviews() {
        Log.e("movie id",mMovie.getId());
        mMovieService.getMovieReviews(mMovie.getId()).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                mReviews = response.body().getReviews();

                for (Review review : mReviews) {
                    Log.e("okay", review.getAuthor());
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                //stop loading refresh loader
                Toast.makeText(MovieDetailsActivity.this, "Error fetching reviews, please try again",
                        Toast.LENGTH_SHORT).show();
                Log.e("error", t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable(AppResource.BUNDLE_STATE, mMovie);
    }
}
