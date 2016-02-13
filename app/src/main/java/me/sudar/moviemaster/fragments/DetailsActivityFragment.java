package me.sudar.moviemaster.fragments;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.sudar.moviemaster.R;
import me.sudar.moviemaster.models.Movie;
import me.sudar.moviemaster.network.TmDbService;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    private View view;

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_details, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        Bundle b = (Bundle) activity.getIntent().getExtras().get("DATA");

        if(b != null) {
            Movie movie = b.getParcelable("MOVIE");

            activity.setTitle(movie.getTitle());

            Picasso.with(activity).load(TmDbService.BACKDROP_IMAGE_BASE_URL + movie.getBackdropPath())
                    .into(((ImageView) activity.findViewById(R.id.backdrop_image_view)));

            String releaseYear = movie.getReleaseDate().split("-")[0];
            ((TextView) view.findViewById(R.id.release_year_text_view)).setText(releaseYear);
            ((TextView) view.findViewById(R.id.rating_text_view))
                    .setText(String.format(getString(R.string.rating_string), movie.getVoteAverage()));

            ((TextView) view.findViewById(R.id.plot_text_view)).setText(movie.getOverview());

            Picasso.with(view.getContext()).load(TmDbService.IMAGE_BASE_URL + movie.getPosterPath())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder).into(((ImageView) view.findViewById(R.id.poster_image_view)));
        }
    }
}
