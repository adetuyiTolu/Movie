package com.crevation.movie.data.rest;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Slimfit on 4/14/2017.
 */

public interface MovieService {

    //@GET("movie/popular?api_key=[API_KEY]")
    @GET("movie/popular?api_key=e3a977c2c0a0af61873aa9b7474ac364")
    Call<MovieResponse> getPopularMovies();

    @GET("movie/top_rated?api_key=[API_KEY]")
    Call<MovieResponse> getTopRatedMovies();

    @GET("movie/{id}/videos?api_key=[API_KEY]")
    Call<TrailerResponse> getMovieTrailers(@Path("id") String id);

    @GET("movie/{id}/reviews?api_key=[API_KEY]")
    Call<ReviewResponse> getMovieReviews(@Path("id") String id);

}
