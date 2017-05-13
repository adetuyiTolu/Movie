package com.crevation.movie.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
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
import com.crevation.movie.data.local.MovieDb;
import com.crevation.movie.data.model.Movie;
import com.crevation.movie.data.rest.MovieResponse;
import com.crevation.movie.data.rest.MovieService;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crevation.movie.AppResource.BY_FAVORITE;
import static com.crevation.movie.AppResource.BY_POPULAR;
import static com.crevation.movie.AppResource.BY_TOP_RATED;

public class MovieActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.movieRecycler)
    RecyclerView mMovieRecycler;

    @InjectView(R.id.movieSwipe)
    SwipeRefreshLayout mMovieSwipe;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    GridLayoutManager mGridLayoutManager;
    MovieAdapter mMovieAdapter;
    ArrayList<Movie> mMovies;
    int SORT_BY = BY_POPULAR;
    MovieService mMovieService;
    MovieDb mMovieDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.inject(this);
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
            mMovies = savedInstanceState.getParcelableArrayList(AppResource.ALL_MOVIES);
            SORT_BY = savedInstanceState.getInt(AppResource.SORT_STATE);
            refreshMovieList();
        } else {
            fetchMovies();
        }
    }

    /**
     * Initialize views and adapters for RecyclerView
     */
    void init() {
        mMovies = new ArrayList<>();
        mMovieAdapter = new MovieAdapter(mMovies, this, this);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mGridLayoutManager = new GridLayoutManager(this, 4);
        } else {
            mGridLayoutManager = new GridLayoutManager(this, 2);
        }

        mMovieRecycler.setLayoutManager(mGridLayoutManager);
        mMovieRecycler.setAdapter(mMovieAdapter);
        mMovieSwipe.setOnRefreshListener(this);
        mMovieService = MovieApp.getMovieService();
        mMovieDb = MovieDb.getInstance(getApplicationContext());
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
        Call<MovieResponse> call = mMovieService.getPopularMovies();
        //change api to call based on user selection via menu
        if (SORT_BY == 2) {
            call = mMovieService.getTopRatedMovies();
        } else if (SORT_BY == 3) {
            sortByFavourite();
            return;
        }
        //start loading refresh loader
        mMovieSwipe.setRefreshing(true);
        //make call to api
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                mMovies = new ArrayList<Movie>();
                try {
                    mMovies.addAll(response.body().getMovies());
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    //fetch from favorite

    private void sortByFavourite() {

        Cursor cursor = mMovieDb.getAllMovies();

        if (cursor.getCount() > 0) {

            mMovies.clear();
            while (cursor.moveToNext()) {
                Movie movie = new Movie(cursor.getString(3));
                movie.setId(cursor.getString(1));
                movie.setTitle(cursor.getString(2));
                movie.setPosterUrl(cursor.getString(3));
                movie.setRatings(cursor.getString(4));
                movie.setOverview(cursor.getString(5));
                movie.setSynopsis(cursor.getString(5));
                movie.setReleaseDate(cursor.getString(6));
                mMovies.add(movie);

            }
            refreshMovieList();
        } else {
            Toast.makeText(this, "You have no favourite movie.", Toast.LENGTH_SHORT).show();
        }
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
            SORT_BY = BY_TOP_RATED;
            fetchMovies();
            return true;
        } else if (id == R.id.action_popular) {
            SORT_BY = BY_POPULAR;
            fetchMovies();
        } else if (id == R.id.action_favorite) {
            SORT_BY = BY_FAVORITE;
            fetchMovies();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelableArrayList(AppResource.ALL_MOVIES, mMovies);
        outState.putInt(AppResource.SORT_STATE, SORT_BY);
    }

}
