package com.crevation.movie.data.rest;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Slimfit on 4/14/2017.
 */

public interface MovieService {

    @GET("movie/popular?api_key=[API_KEY]")
   Call<MovieResponse> getPopularMovies();

    @GET("movie/top_rated?api_key=[API_KEY]")
    Call<MovieResponse> getTopRatedMovies();

}
