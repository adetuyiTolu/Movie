package com.crevation.movie.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Slimfit on 4/13/2017.
 */

public class Movie implements Parcelable {

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

    private String id;

    private boolean favorite;

    public Movie(Parcel parcel) {
        this.releaseDate = parcel.readString();
        this.title = parcel.readString();
        this.overview = parcel.readString();
        this.posterUrl = parcel.readString();
        this.ratings = parcel.readString();
        this.synopsis = parcel.readString();
        this.id = parcel.readString();

    }

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

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(releaseDate);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(posterUrl);
        parcel.writeString(ratings);
        parcel.writeString(synopsis);
        parcel.writeString(id);

    }


}
