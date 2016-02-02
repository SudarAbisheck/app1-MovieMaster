package me.sudar.moviemaster.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import me.sudar.moviemaster.models.ApiCallReply;
import me.sudar.moviemaster.MovieMasterApplication;
import me.sudar.moviemaster.R;
import me.sudar.moviemaster.network.TmDbService;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private MenuItem popMovieMenu;
    private MenuItem highRatedMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sp = getSharedPreferences("MovieMaster", Context.MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        popMovieMenu = menu.findItem(R.id.action_pop_movies);
        highRatedMenu = menu.findItem(R.id.action_high_rated_movies);
        int option = sp.getInt("MovieListOption", 0);
        if(option == 0) {
            popMovieMenu.setEnabled(false);
            loadPopularMovies();
        }
        else if(option == 1) {
            highRatedMenu.setEnabled(false);
            loadHighRatedMovies();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_pop_movies) {
            loadPopularMovies();
            return true;
        }else if(id == R.id.action_high_rated_movies){
            loadHighRatedMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadPopularMovies(){
        TmDbService
                .getService()
                .getPopularMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiCallReply>() {
                    @Override
                    public void onCompleted() {
                        popMovieMenu.setEnabled(false);
                        highRatedMenu.setEnabled(true);
                        sp.edit().putInt("MovieListOption",0).apply();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ApiCallReply apiCallReply) {
                        Log.i("MAIN", apiCallReply.getMovies().get(0).getOriginalTitle());
                        MovieMasterApplication.movieGridAdapter.updateData(apiCallReply.getMovies());
                    }
                });
    }

    public void loadHighRatedMovies(){
        TmDbService
                .getService()
                .getHighRatedMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiCallReply>() {
                    @Override
                    public void onCompleted() {
                        popMovieMenu.setEnabled(true);
                        highRatedMenu.setEnabled(false);
                        sp.edit().putInt("MovieListOption",1).apply();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ApiCallReply apiCallReply) {
                        Log.i("MAIN", apiCallReply.getMovies().get(0).getOriginalTitle());
                        MovieMasterApplication.movieGridAdapter.updateData(apiCallReply.getMovies());
                    }
                });
    }
}
