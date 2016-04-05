package me.sudar.moviemaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import me.sudar.moviemaster.models.Movie;

/**
 * Created by sudar on 8/3/16.
 * Email : hey@sudar.me
 */
public class FavMovieDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "MovieDB";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "FavMovies";
    private static final String FIELD_POSTER_PATH = "posterPath";
    private static final String FIELD_OVERVIEW = "overview";
    private static final String FIELD_RELEASE_DATE = "releaseDate";
    private static final String FIELD_ID= "id";
    private static final String FIELD_TITLE = "title";
    private static final String FIELD_BACKDROP_PATH = "backdropPath";
    private static final String FIELD_VOTE_AVERAGE = "voteAverage";


    public FavMovieDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "create table " + TABLE_NAME + "("
                + FIELD_POSTER_PATH + " text,"
                + FIELD_OVERVIEW + " text,"
                + FIELD_RELEASE_DATE + " text,"
                + FIELD_ID + " integer primary key,"
                + FIELD_TITLE + " text,"
                + FIELD_BACKDROP_PATH + " text,"
                + FIELD_VOTE_AVERAGE + " real" + ")";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public void insertMovie(Movie movie){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_POSTER_PATH, movie.getPosterPath());
        contentValues.put(FIELD_OVERVIEW, movie.getOverview());
        contentValues.put(FIELD_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(FIELD_ID, movie.getId());
        contentValues.put(FIELD_TITLE, movie.getTitle());
        contentValues.put(FIELD_BACKDROP_PATH, movie.getBackdropPath());
        contentValues.put(FIELD_VOTE_AVERAGE, movie.getVoteAverage());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    public boolean removeMovie(int movieId){
        boolean result = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + FIELD_ID + " = \"" + movieId + "\"", null);
        if(cursor.moveToFirst()){
            db.delete(TABLE_NAME, FIELD_ID + " = ? ", new String[]{String.valueOf(cursor.getInt(3))});
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }

    public ArrayList<Movie> getAllMovies(){
        ArrayList<Movie> movies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME , null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Movie movie  = new Movie();
            movie.setPosterPath(cursor.getString(0));
            movie.setOverview(cursor.getString(1));
            movie.setReleaseDate(cursor.getString(2));
            movie.setId(cursor.getInt(3));
            movie.setTitle(cursor.getString(4));
            movie.setBackdropPath(cursor.getString(5));
            movie.setVoteAverage(cursor.getDouble(6));
            movies.add(movie);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return movies;
    }

    public boolean isPresent(int movieId){
        boolean result = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + FIELD_ID + " = \"" + movieId + "\"", null);
        if(cursor.moveToFirst()){
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }
}
