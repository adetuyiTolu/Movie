package com.crevation.movie.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Slimfit on 4/13/2017.
 */

public class Movie implements Serializable {

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("original_title")
    private String title;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterUrl;

    @SerializedName("vote_average")
    private String ratings;

    private String synopsis;

    public Movie(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRatings() {
        return ratings;
    }

    public String getTitle() {
        return title;
    }
}
