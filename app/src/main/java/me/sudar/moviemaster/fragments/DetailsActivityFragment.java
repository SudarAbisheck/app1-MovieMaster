package me.sudar.moviemaster.fragments;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.sudar.moviemaster.R;
import me.sudar.moviemaster.activities.DetailsActivity;
import me.sudar.moviemaster.adapters.TrailerListAdapter;
import me.sudar.moviemaster.database.FavMovieDB;
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
    private ContentLoadingProgressBar trailerProgressBar;
    private TextView trailerInfoView;
    private RecyclerView trailerListView;

    private ContentLoadingProgressBar reviewProgressBar;
    private TextView reviewInfoView;
    private LinearLayout reviewListLL;
    private ImageView favToggle;

    private TrailerListAdapter trailerListAdapter;
    private ArrayList<Trailer> trailerList = new ArrayList<>();
    private ArrayList<Review> reviewList = new ArrayList<>();
    private Movie movie;
    private FavMovieDB db;

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_details, container, false);

        trailerListView = (RecyclerView) view.findViewById(R.id.trailer_list_view);
        trailerProgressBar = (ContentLoadingProgressBar) view.findViewById(R.id.trailerProgressBar);
        trailerInfoView = (TextView) view.findViewById(R.id.trailerInfoView);

        trailerListView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        trailerListAdapter = new TrailerListAdapter();
        trailerListView.setAdapter(trailerListAdapter);

        reviewListLL = (LinearLayout) view.findViewById(R.id.review_list_linear_layout);
        reviewProgressBar = (ContentLoadingProgressBar) view.findViewById(R.id.reviewProgressBar);
        reviewInfoView = (TextView) view.findViewById(R.id.reviewInfoView);

        db = new FavMovieDB(view.getContext());
        favToggle = (ImageView) view.findViewById(R.id.fav_toggle);
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
            trailerList =  savedInstanceState.getParcelableArrayList("MOVIE_TRAILER_LIST");
            trailerListAdapter.updateData(trailerList);
            trailerProgressBar.setVisibility(View.GONE);
            if(trailerList.size() > 0)
                trailerListView.setVisibility(View.VISIBLE);
            else if (trailerList.size() == 0){
                trailerInfoView.setVisibility(View.VISIBLE);
                trailerInfoView.setText(getResources().getString(R.string.nothing_found));
            }

            reviewList =  savedInstanceState.getParcelableArrayList("MOVIE_REVIEW_LIST");
            updateReview(reviewList);
            reviewProgressBar.setVisibility(View.GONE);
            if(reviewList.size() > 0)
                reviewListLL.setVisibility(View.VISIBLE);
            else if (reviewList.size() == 0 ){
                reviewInfoView.setVisibility(View.VISIBLE);
                reviewInfoView.setText(getResources().getString(R.string.nothing_found));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE_PARCEL, movie);
        outState.putParcelableArrayList("MOVIE_TRAILER_LIST", trailerList);
        outState.putParcelableArrayList("MOVIE_REVIEW_LIST", reviewList);
    }

    public void setScreen(final Movie movie, Activity activity){

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

        if(db.isPresent(movie.getId()))
            favToggle.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.heart_filled));
        else
            favToggle.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.heart));

        favToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.isPresent(movie.getId())){
                    db.removeMovie(movie.getId());
                    favToggle.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.heart));
                }else{
                    db.insertMovie(movie);
                    favToggle.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.heart_filled));
                }
            }
        });
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
                        trailerProgressBar.setVisibility(View.GONE);
                        if(trailerList.size() > 0)
                            trailerListView.setVisibility(View.VISIBLE);
                        else if (trailerList.size() == 0){
                            trailerInfoView.setVisibility(View.VISIBLE);
                            trailerInfoView.setText(getResources().getString(R.string.nothing_found));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        trailerProgressBar.setVisibility(View.GONE);
                        trailerInfoView.setVisibility(View.VISIBLE);
                        trailerInfoView.setText(getResources().getString(R.string.internet_error_message));
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
                        reviewProgressBar.setVisibility(View.GONE);
                        if(reviewList.size() > 0)
                            reviewListLL.setVisibility(View.VISIBLE);
                        else if (reviewList.size() == 0 ){
                            reviewInfoView.setVisibility(View.VISIBLE);
                            reviewInfoView.setText(getResources().getString(R.string.nothing_found));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        reviewProgressBar.setVisibility(View.GONE);
                        reviewInfoView.setVisibility(View.VISIBLE);
                        reviewInfoView.setText(getResources().getText(R.string.internet_error_message));
                    }

                    @Override
                    public void onNext(ReviewReply reviewReply) {
                        reviewList = (ArrayList<Review>) reviewReply.getReviews();
                        updateReview(reviewList);
                    }
                });

    }
}
