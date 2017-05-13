package com.crevation.movie.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crevation.movie.AppResource;
import com.crevation.movie.MovieApp;
import com.crevation.movie.R;
import com.crevation.movie.adapter.ReviewAdapter;
import com.crevation.movie.adapter.TrailerAdapter;
import com.crevation.movie.data.local.MovieDb;
import com.crevation.movie.data.model.Movie;
import com.crevation.movie.data.model.Review;
import com.crevation.movie.data.model.Trailer;
import com.crevation.movie.data.rest.MovieService;
import com.crevation.movie.data.rest.ReviewResponse;
import com.crevation.movie.data.rest.TrailerResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crevation.movie.AppResource.YOUTUBE_URL;

public class MovieDetailsActivity extends AppCompatActivity implements TrailerAdapter.OnItemClickListener {
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

    @InjectView(R.id.reviewsRecycler)
    RecyclerView mReviewRecycler;

    @InjectView(R.id.trailersRecycler)
    RecyclerView mtrailersRecycler;

    @InjectView(R.id.reviewSwipe)
    SwipeRefreshLayout mReviewSwipe;

    @InjectView(R.id.trailerSwipe)
    SwipeRefreshLayout mTrailerSwipe;

    @InjectView(R.id.mark_favorite)
    ImageView mFavorite;

    MovieService mMovieService;
    ArrayList<Review> mReviews;
    ArrayList<Trailer> mTrailers;
    TrailerAdapter mTrailerAdapter;
    ReviewAdapter mReviewAdapter;
    LinearLayoutManager mReviewLayoutManager;
    LinearLayoutManager mTrailerLayoutManager;
    MovieDb mMovieDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getBundles(savedInstanceState);
        init();
        fetchExtraDetails();
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

        //initialize service
        mMovieService = MovieApp.getMovieService();
        mMovieDb = MovieDb.getInstance(getApplicationContext());

        //initialize Trailer View
        mTrailers = new ArrayList<>();
        mTrailerLayoutManager = new LinearLayoutManager(this);
        mTrailerAdapter = new TrailerAdapter(mTrailers, this, this);
        mtrailersRecycler.setLayoutManager(mTrailerLayoutManager);
        mtrailersRecycler.setAdapter(mTrailerAdapter);
        mtrailersRecycler.setNestedScrollingEnabled(true);

        //initialize Review View
        mReviews = new ArrayList<>();
        mReviewLayoutManager = new LinearLayoutManager(this);
        mReviewAdapter = new ReviewAdapter(mReviews, this);
        mReviewRecycler.setLayoutManager(mReviewLayoutManager);
        mReviewRecycler.setAdapter(mReviewAdapter);
        mtrailersRecycler.setNestedScrollingEnabled(true);

        //favorite
        if (mMovieDb.isLocal(mMovie.getId())) {
            mFavorite.setImageResource(R.drawable.ic_favorite_black);
            mMovie.setFavorite(true);
        } else {
            mFavorite.setImageResource(R.drawable.ic_favorite_white);
            mMovie.setFavorite(false);
        }
        mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favoriteMovie();
            }
        });

    }

    void fetchExtraDetails() {
        if (!MovieApp.getInstance().isOnline(this)) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            mReviewSwipe.setRefreshing(false);
            mTrailerSwipe.setRefreshing(false);
            return;
        }
        fetchReviews();
        fetchTrailers();
    }

    void fetchReviews() {

        mReviewSwipe.setRefreshing(true);

        mMovieService.getMovieReviews(mMovie.getId()).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                mReviews = response.body().getReviews();
                for (Review review : mReviews) {
                    Toast.makeText(getApplicationContext(), review.getContent() + "yeah", Toast.LENGTH_SHORT).show();
                    Log.d("here", review.getContent());
                }
                mReviewAdapter = new ReviewAdapter(mReviews, MovieDetailsActivity.this);
                mReviewRecycler.setAdapter(mReviewAdapter);
                mReviewSwipe.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                //stop loading refresh loader
                Toast.makeText(MovieDetailsActivity.this, "Error fetching reviews, please try again",
                        Toast.LENGTH_SHORT).show();
                Log.e("error", t.getLocalizedMessage());
                mReviewSwipe.setRefreshing(false);
            }
        });
    }

    void fetchTrailers() {
        mTrailerSwipe.setRefreshing(true);
        mMovieService.getMovieTrailers(mMovie.getId()).enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                mTrailers = response.body().getTrailers();
                mTrailerAdapter = new TrailerAdapter(mTrailers, MovieDetailsActivity.this, MovieDetailsActivity.this);
                mtrailersRecycler.setAdapter(mTrailerAdapter);
                mTrailerSwipe.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                //stop loading refresh loader
                Toast.makeText(MovieDetailsActivity.this, "Error fetching trailers, please try again",
                        Toast.LENGTH_SHORT).show();
                Log.e("error", t.getLocalizedMessage());
                mTrailerSwipe.setRefreshing(false);
            }
        });
    }

    void favoriteMovie() {
        if (mMovie.isFavorite()) {
            Toast.makeText(MovieDetailsActivity.this, "Movie is already on favorite list",
                    Toast.LENGTH_SHORT).show();
        } else {
            mMovieDb.saveMovie(mMovie);
        }
    }

    @Override
    public void OnClick(int itemPosition) {
        //watch trailer on youtube
        String url = YOUTUBE_URL + mTrailers.get(itemPosition).getKey();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "View Trailer"));
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable(AppResource.BUNDLE_STATE, mMovie);
    }
}
