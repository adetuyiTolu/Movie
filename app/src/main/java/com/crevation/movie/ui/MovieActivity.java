package com.crevation.movie.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.crevation.movie.AppResource;
import com.crevation.movie.MovieApp;
import com.crevation.movie.R;
import com.crevation.movie.adapter.MovieAdapter;
import com.crevation.movie.data.model.Movie;
import com.crevation.movie.data.rest.MovieResponse;
import com.crevation.movie.data.rest.MovieService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    RecyclerView mMovieRecycler;
    MovieAdapter mMovieAdapter;
    ArrayList<Movie> mMovies;
    SwipeRefreshLayout mMovieSwipe;
    boolean SORT_BY_POPULAR = true;
    MovieService mMovieService;
    GridLayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        getSavedBundles(savedInstanceState);
    }


    /**
     * get saved instance and load movies
     *
     * @param savedInstanceState saved instance state
     */
    void getSavedBundles(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mMovies = (ArrayList<Movie>) savedInstanceState.getSerializable(AppResource.ALL_MOVIES);
            SORT_BY_POPULAR = savedInstanceState.getBoolean(AppResource.SORT_STATE);
            refreshMovieList();
        } else {
            fetchMovies();
        }
    }

    /**
     * Initialize views and adapters for RecyclerView
     */
    void init() {
        mMovieSwipe = (SwipeRefreshLayout) findViewById(R.id.movieSwipe);
        mMovieRecycler = (RecyclerView) findViewById(R.id.movieRecycler);
        mMovies = new ArrayList<>();
        mMovieAdapter = new MovieAdapter(mMovies, this, this);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            mGridLayoutManager = new GridLayoutManager(this, 4);
        }else{
            mGridLayoutManager = new GridLayoutManager(this, 2);
        }

        mMovieRecycler.setLayoutManager(mGridLayoutManager);
        mMovieRecycler.setAdapter(mMovieAdapter);
        mMovieSwipe.setOnRefreshListener(this);
        mMovieService = MovieApp.getMovieService();
    }

    @Override
    public void OnClick(int itemPosition) {
        Movie selectedMovie = mMovies.get(itemPosition);
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(AppResource.BUNDLE_DETAILS, selectedMovie);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        fetchMovies();
    }

    /**
     * fetch all movies
     */
    void fetchMovies() {
        if (!MovieApp.getInstance().isOnline(this)) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            mMovieSwipe.setRefreshing(false);
            return;
        }
        Call<MovieResponse> call = mMovieService.getTopRatedMovies();
        //change api to call based on user selection via menu
        if (SORT_BY_POPULAR) {
            call = mMovieService.getPopularMovies();
        }
        //start loading refresh loader
        mMovieSwipe.setRefreshing(true);
        //make call to api
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                mMovies = new ArrayList<Movie>();
                mMovies.addAll(response.body().getMovies());
                refreshMovieList();
                //stop loading refresh loader
                mMovieSwipe.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                //stop loading refresh loader
                mMovieSwipe.setRefreshing(false);
                Toast.makeText(MovieActivity.this, "Error fetching movies, please try again",
                        Toast.LENGTH_SHORT).show();
                Log.e("error", t.getLocalizedMessage());
            }
        });
    }

    void refreshMovieList() {
        mMovieAdapter = new MovieAdapter(mMovies, this, this);
        mMovieRecycler.setAdapter(mMovieAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        item.setChecked(true);
        if (id == R.id.action_top_rated) {
            SORT_BY_POPULAR = false;
            fetchMovies();
            return true;
        } else if (id == R.id.action_popular) {
            SORT_BY_POPULAR = true;
            fetchMovies();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable(AppResource.ALL_MOVIES, mMovies);
        outState.putBoolean(AppResource.SORT_STATE, SORT_BY_POPULAR);
    }

}
