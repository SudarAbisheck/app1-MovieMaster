package me.sudar.moviemaster.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import me.sudar.moviemaster.R;
import me.sudar.moviemaster.fragments.DetailsActivityFragment;
import me.sudar.moviemaster.models.Movie;
import me.sudar.moviemaster.network.TmDbService;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle b = (Bundle) getIntent().getExtras().get("DATA");
        Movie movie = (Movie) b.getSerializable("MOVIE");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(movie.getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Picasso.with(this).load(TmDbService.IMAGE_BASE_URL + movie.getBackdropPath())
                .into(((ImageView) findViewById(R.id.backdrop_image_view)));
    }

}
