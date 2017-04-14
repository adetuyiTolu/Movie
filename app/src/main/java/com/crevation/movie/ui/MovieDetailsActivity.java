package com.crevation.movie.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.crevation.movie.AppResource;
import com.crevation.movie.R;
import com.crevation.movie.data.model.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {
    Movie mMovie;
    TextView mTitle, mDate, mContent;
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getBundles(savedInstanceState);
        init();
    }

    void getBundles(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mMovie = (Movie) savedInstanceState.getSerializable(AppResource.BUNDLE_STATE);
        } else {
            mMovie = (Movie) getIntent().getSerializableExtra(AppResource.BUNDLE_DETAILS);
        }
    }

    void init() {
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.toolbar_layout);
        mTitle = (TextView) findViewById(R.id.title);
        mDate = (TextView) findViewById(R.id.date);
        mContent = (TextView) findViewById(R.id.content);
        mImageView = (ImageView) collapsingToolbarLayout.findViewById(R.id.mov_image);
        mTitle.setText(mMovie.getTitle());
        mDate.setText(mMovie.getReleaseDate());
        mContent.setText(mMovie.getOverview());
        setTitle("Rated " + mMovie.getRatings() + " out of 10");
        Picasso.with(this).load(AppResource.IMAGE_PATH + mMovie.getPosterUrl())
                .placeholder(R.drawable.movie)
                .error(R.drawable.movie).into(mImageView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable(AppResource.BUNDLE_STATE, mMovie);
    }
}
