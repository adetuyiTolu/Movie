package com.crevation.movie.data.rest;

import com.crevation.movie.data.model.Movie;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Slimfit on 4/14/2017.
 */

public class MovieResponse implements Serializable{

    @SerializedName("results")
    private ArrayList<Movie> movies;

    public ArrayList<Movie> getMovies() {
        return movies;
    }
}
