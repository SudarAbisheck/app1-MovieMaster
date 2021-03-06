package me.sudar.moviemaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import me.sudar.moviemaster.fragments.DetailsActivityFragment;
import me.sudar.moviemaster.fragments.ErrorFragment;
import me.sudar.moviemaster.fragments.MovieGridFragment;
import me.sudar.moviemaster.R;
import me.sudar.moviemaster.models.Movie;

public class MainActivity extends AppCompatActivity implements MovieGridFragment.CallBacks{

    private DetailsActivityFragment detailsActivityFragment;
    private boolean twoPane = false;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(findViewById(R.id.movie_detail_frame) != null){
            twoPane = true;
            if(savedInstanceState == null) {

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.movie_detail_frame, new ErrorFragment())
                        .commit();

            }else{
                detailsActivityFragment = (DetailsActivityFragment) fragmentManager.getFragment(savedInstanceState,"MOVIE_DETAIL_FRAG");

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.movie_detail_frame, detailsActivityFragment)
                        .commit();
            }

            MovieGridFragment movieGridFragment = (MovieGridFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.movie_grid_fragment);
            movieGridFragment.setSelectedItem(true);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(twoPane)
            fragmentManager.putFragment(outState,"MOVIE_DETAIL_FRAG", detailsActivityFragment);
    }

    @Override
    public void onItemSelected(Movie movie) {
        if(twoPane){
            Bundle bundle = new Bundle();
            bundle.putParcelable(DetailsActivityFragment.MOVIE_PARCEL,movie);
            detailsActivityFragment = new DetailsActivityFragment();
            detailsActivityFragment.setArguments(bundle);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.movie_detail_frame,detailsActivityFragment)
                    .commit();
        }else{
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(DetailsActivityFragment.MOVIE_PARCEL,movie);
            startActivity(intent);
        }
    }

    @Override
    public void onListLoaded(Movie firstInTheList) {
        if(twoPane) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(DetailsActivityFragment.MOVIE_PARCEL, firstInTheList);
            detailsActivityFragment = new DetailsActivityFragment();
            detailsActivityFragment.setArguments(bundle);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.movie_detail_frame, detailsActivityFragment)
                    .commit();
        }
    }

    @Override
    public void onLoadingFailed() {
        if(twoPane){
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.movie_detail_frame, new ErrorFragment())
                    .commit();
        }
    }
}
