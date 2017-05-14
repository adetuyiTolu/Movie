package com.crevation.movie.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.crevation.movie.data.model.Movie;

import static com.crevation.movie.data.local.MovieContract.ContactEntry.COLUMN_ID;
import static com.crevation.movie.data.local.MovieContract.ContactEntry.COLUMN_MOVIE_ID;
import static com.crevation.movie.data.local.MovieContract.ContactEntry.COLUMN_POSTER_URL;
import static com.crevation.movie.data.local.MovieContract.ContactEntry.COLUMN_RATING;
import static com.crevation.movie.data.local.MovieContract.ContactEntry.COLUMN_RELEASE_DATE;
import static com.crevation.movie.data.local.MovieContract.ContactEntry.COLUMN_SYNOPSIS;
import static com.crevation.movie.data.local.MovieContract.ContactEntry.COLUMN_TITLE;
import static com.crevation.movie.data.local.MovieContract.ContactEntry.TABLE_NAME;

/**
 * Created by Slimfit on 5/13/2017.
 */

public class MovieDb {
    MovieDbHelper dbHelper;
    private static MovieDb INSTANCE;

    private MovieDb(Context context) {

       dbHelper = new MovieDbHelper(context);

    }

    public static MovieDb getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MovieDb(context);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public boolean saveMovie(Movie movie) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(COLUMN_POSTER_URL, movie.getPosterUrl());
        contentValues.put(COLUMN_TITLE, movie.getTitle());
        contentValues.put(COLUMN_RATING, movie.getRatings());
        contentValues.put(COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(COLUMN_SYNOPSIS, movie.getSynopsis());

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllMovies() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC", null);
        return res;
    }

    public boolean deleteMovie(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_NAME, "movie_id=?", new String[]{id});
        return true;
    }

    public boolean isLocal(String m_id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " WHERE movie_id=" + m_id, null);
        return res.getCount() > 0;
    }

    public String getLocalMovieID(String movieId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select id from " + TABLE_NAME + " WHERE movie_id=" + movieId, null);
        while (cursor.moveToNext()) {
            return String.valueOf(cursor.getInt(0));
        }
        return null;
    }
}
