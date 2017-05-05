package com.crevation.movie.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Slimfit on 5/1/2017.
 */

public class Review implements Parcelable {
    private String id;
    private String author;
    private String content;

    public Review(Parcel parcel) {
        parcel.readString();
        parcel.readString();
        parcel.readString();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Object[] newArray(int i) {
            return new Object[0];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(author);
        parcel.writeString(content);
    }
}
