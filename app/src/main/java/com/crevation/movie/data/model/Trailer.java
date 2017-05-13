package com.crevation.movie.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Slimfit on 5/1/2017.
 */

public class Trailer implements Parcelable {
    private String id;
    private String key;
    private String type;

    public Trailer(Parcel parcel) {
        parcel.readString();
        parcel.readString();
        parcel.readString();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel parcel) {
            return new Trailer(parcel);
        }

        @Override
        public Object[] newArray(int i) {
            return new Object[0];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(key);
        parcel.writeString(type);
    }
}
