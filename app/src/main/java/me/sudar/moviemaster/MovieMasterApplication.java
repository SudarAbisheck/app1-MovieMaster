package me.sudar.moviemaster;

import android.app.Application;

import me.sudar.moviemaster.adapters.MovieGridAdapter;

/**
 * Created by sudar on 1/2/16.
 * Email : hey@sudar.me
 */
public class MovieMasterApplication extends Application {
    public static MovieGridAdapter movieGridAdapter = new MovieGridAdapter();
}
