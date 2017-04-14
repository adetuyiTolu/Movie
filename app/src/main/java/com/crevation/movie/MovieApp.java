package com.crevation.movie;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;

import com.crevation.movie.data.model.Movie;
import com.crevation.movie.data.rest.MovieService;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.crevation.movie.AppResource.BASE_URL;

/**
 * Created by Adetuyi Tolu on 3/04/2017.
 */

public class MovieApp extends Application {

    private static Retrofit retrofit = null;
    private OkHttpClient client;
    private static MovieApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initRetrofit();
    }

    public static MovieApp getInstance() {
        if (instance == null) {
            instance = new MovieApp();
        }
        return instance;
    }


    /**
     * Setup retrofit and cache
     */
    private void initRetrofit() {

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    /**
     * @return the Retrofit Rest Api Service
     */
    public static MovieService getMovieService() {
        return retrofit.create(MovieService.class);
    }


    public boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenet = connectivityManager.getActiveNetworkInfo();
        return activenet != null;
    }
}
