package com.crevation.movie.data.rest;

import android.os.Parcel;
import android.os.Parcelable;

import com.crevation.movie.data.model.Movie;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Slimfit on 4/14/2017.
 */

public class MovieResponse implements Parcelable {

    @SerializedName("results")
    private ArrayList<Movie> movies;

    public MovieResponse(Parcel parcel) {
        movies=new ArrayList<>();
        parcel.readTypedList(movies, Movie.CREATOR);
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<MovieResponse> CREATOR = new Creator<MovieResponse>() {
        @Override
        public MovieResponse createFromParcel(Parcel parcel) {
            return null;
        }

        @Override
        public MovieResponse[] newArray(int i) {
            return new MovieResponse[0];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(movies);
    }
}
