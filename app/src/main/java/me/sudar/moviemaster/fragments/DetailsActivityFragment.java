package me.sudar.moviemaster.fragments;

import android.app.Activity;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.sudar.moviemaster.R;
import me.sudar.moviemaster.activities.DetailsActivity;
import me.sudar.moviemaster.adapters.TrailerListAdapter;
import me.sudar.moviemaster.models.Movie;
import me.sudar.moviemaster.models.Review;
import me.sudar.moviemaster.models.ReviewReply;
import me.sudar.moviemaster.models.Trailer;
import me.sudar.moviemaster.models.TrailerReply;
import me.sudar.moviemaster.network.TmDbService;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class DetailsActivityFragment extends Fragment {

    public static final String MOVIE_PARCEL = "MOVIE_PARCEL";

    private View view;
    private LinearLayout trailerContainer;
    private LinearLayout reviewContainer;
    private LinearLayout reviewListLL;

    private TrailerListAdapter trailerListAdapter;
    private ArrayList<Trailer> trailerList = new ArrayList<>();
    private ArrayList<Review> reviewList = new ArrayList<>();
    private Movie movie;

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

        reviewContainer = (LinearLayout) view.findViewById(R.id.review_container);
        reviewListLL = (LinearLayout) view.findViewById(R.id.review_list_linear_layout);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();

        if(savedInstanceState == null) {
            movie = activity.getIntent().getParcelableExtra(MOVIE_PARCEL);

            if (movie != null) {
                setScreen(movie, activity);
                loadTrailers(movie.getId());
                loadReviews(movie.getId());
            }else if(getArguments().containsKey(MOVIE_PARCEL)) {
                movie = getArguments().getParcelable(MOVIE_PARCEL);
                if (movie != null) {
                    setScreen(movie, activity);
                    loadTrailers(movie.getId());
                    loadReviews(movie.getId());
                }
            }
        }else{
            movie = savedInstanceState.getParcelable(MOVIE_PARCEL);
            if (movie != null) {
                setScreen(movie, activity);
            }
            ArrayList<Trailer> trailerListTemp =  savedInstanceState.getParcelableArrayList("MOVIE_TRAILER_LIST");
            trailerListAdapter.updateData(trailerListTemp);
            if(trailerListTemp.size() > 0)
                trailerContainer.setVisibility(View.VISIBLE);
            ArrayList<Review> reviewListTemp =  savedInstanceState.getParcelableArrayList("MOVIE_REVIEW_LIST");
            updateReview(reviewListTemp);
            if(reviewListTemp.size() > 0)
                reviewContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE_PARCEL, movie);
        outState.putParcelableArrayList("MOVIE_TRAILER_LIST", trailerList);
        outState.putParcelableArrayList("MOVIE_REVIEW_LIST", reviewList);
    }

    public void setScreen(Movie movie, Activity activity){

        if(activity instanceof DetailsActivity) {
            Picasso.with(activity).load(TmDbService.BACKDROP_IMAGE_BASE_URL + movie.getBackdropPath())
                    .into(((ImageView) activity.findViewById(R.id.backdrop_image_view)));
            activity.setTitle(movie.getTitle());
        }else{
            TextView titleTextView = (TextView) activity.findViewById(R.id.title_text_view);
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(movie.getTitle());
        }

        String releaseYear = movie.getReleaseDate().split("-")[0];
        ((TextView) view.findViewById(R.id.release_year_text_view)).setText(releaseYear);
        ((TextView) view.findViewById(R.id.rating_text_view))
                .setText(String.format(getString(R.string.rating_string), movie.getVoteAverage()));

        ((TextView) view.findViewById(R.id.plot_text_view)).setText(movie.getOverview());

        Picasso.with(view.getContext()).load(TmDbService.IMAGE_BASE_URL + movie.getPosterPath())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).into(((ImageView) view.findViewById(R.id.poster_image_view)));
    }

    public void updateReview(ArrayList<Review> reviewList){
        for(int i = 0; i< reviewList.size(); i++) {
            String raw = String.format(getString(R.string.review_author_string),reviewList.get(i).getAuthor());

            CardView cv = (CardView) LayoutInflater.from(view.getContext()).inflate(R.layout.review_item_layout, reviewListLL, false);
            ((TextView) cv.findViewById(R.id.review_author_tv)).setText(Html.fromHtml(raw));
            ((TextView) cv.findViewById(R.id.review_content_tv)).setText(reviewList.get(i).getContent());

            reviewListLL.addView(cv);
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
                        trailerList = (ArrayList<Trailer>) trailerReply.getTrailers();
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
                        if(reviewList.size() > 0)
                            reviewContainer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ReviewReply reviewReply) {
                        reviewList = (ArrayList<Review>) reviewReply.getReviews();
                        updateReview(reviewList);
                    }
                });

    }
}
