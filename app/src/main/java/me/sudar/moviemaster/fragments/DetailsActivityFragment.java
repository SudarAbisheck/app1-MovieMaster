package me.sudar.moviemaster.fragments;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.sudar.moviemaster.R;
import me.sudar.moviemaster.adapters.TrailerListAdapter;
import me.sudar.moviemaster.models.Movie;
import me.sudar.moviemaster.models.ReviewReply;
import me.sudar.moviemaster.models.Trailer;
import me.sudar.moviemaster.models.TrailerReply;
import me.sudar.moviemaster.network.TmDbService;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    private View view;
    private LinearLayout trailerContainer;

    private TrailerListAdapter trailerListAdapter;
    private ArrayList<Trailer> trailerList = new ArrayList<>();

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_details, container, false);
        trailerContainer = (LinearLayout) view.findViewById(R.id.trailer_container);
        RecyclerView trailerListView = (RecyclerView) view.findViewById(R.id.trailer_list_view);
        trailerListView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        trailerListAdapter = new TrailerListAdapter();
        trailerListView.setAdapter(trailerListAdapter);
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

            loadTrailers(movie.getId());
            loadReviews(movie.getId());
        }
    }

    public void loadTrailers(Integer id){
        TmDbService
                .getService()
                .getTrailers(id)
                .cache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TrailerReply>() {
                    @Override
                    public void onCompleted() {
                        if(trailerList.size() > 0)
                            trailerContainer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(TrailerReply trailerReply) {
//                        trailerListAdapter.updateData((ArrayList<Trailer>) trailerReply.getTrailers());
                        trailerList = (ArrayList<Trailer>) trailerReply.getTrailers();
//                        for(int i =0; i<trailerList.size(); i++)
//                            Log.i("TTTTT", trailerList.get(i).getName());
                        trailerListAdapter.updateData(trailerList);
                    }
                });

    }

    public void loadReviews(Integer id){
        TmDbService
                .getService()
                .getReviews(id)
                .cache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ReviewReply>() {
                    @Override
                    public void onCompleted() {
                        Log.i("TTTTTTTTTTT","COMPLETED");
//                        if(trailerList.size() > 0)
//                            trailerContainer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("TTTTTTTTTTT","FAILED: " + e.getMessage() + "PPPP");
                    }

                    @Override
                    public void onNext(ReviewReply reviewReply) {
//                        trailerListAdapter.updateData((ArrayList<Trailer>) trailerReply.getTrailers());
//                        trailerList = (ArrayList<Trailer>) trailerReply.getTrailers();
//                        for(int i =0; i<trailerList.size(); i++)
//                            Log.i("TTTTT", trailerList.get(i).getName());
//                        trailerListAdapter.updateData(trailerList);
                    }
                });

    }
}
