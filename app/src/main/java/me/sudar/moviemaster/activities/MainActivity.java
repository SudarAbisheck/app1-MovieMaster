package me.sudar.moviemaster.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import me.sudar.moviemaster.fragments.MovieGridFragment;
import me.sudar.moviemaster.models.ApiCallReply;
import me.sudar.moviemaster.MovieMasterApplication;
import me.sudar.moviemaster.R;
import me.sudar.moviemaster.models.Movie;
import me.sudar.moviemaster.network.TmDbService;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

//    private MovieGridFragment movieGridFragment = (MovieGridFragment) getSupportFragmentManager().findFragmentById(R.id.movie_grid_fragment);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        if(savedInstanceState != null){
//            Toast.makeText(this, "Activity saveInstanceState", Toast.LENGTH_SHORT).show();
//            movieGridFragment = (MovieGridFragment) getSupportFragmentManager().getFragment(savedInstanceState,"MOVIE_GRID_FRAG");
//
//        }else{
//            Toast.makeText(this, "Activity saveInstanceState null", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        getSupportFragmentManager().putFragment(outState,"MOVIE_GRID_FRAG", movieGridFragment);
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        MenuItem popMovieMenu = menu.findItem(R.id.action_pop_movies);
//        MenuItem highRatedMenu = menu.findItem(R.id.action_high_rated_movies);
//
//        if(MovieMasterApplication.selectedMenu == 0) {
//            popMovieMenu.setEnabled(false);
//        }
//        else if(MovieMasterApplication.selectedMenu == 1) {
//            highRatedMenu.setEnabled(false);
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_pop_movies) {
//            movieGridFragment.loadPopularMovies();
//            return true;
//        }else if(id == R.id.action_high_rated_movies){
//            movieGridFragment.loadHighRatedMovies();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
