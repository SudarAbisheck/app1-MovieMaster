package me.sudar.moviemaster.fragments;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.sudar.moviemaster.MovieMasterApplication;
import me.sudar.moviemaster.R;
import me.sudar.moviemaster.adapters.MovieGridAdapter;
import me.sudar.moviemaster.models.ApiCallReply;
import me.sudar.moviemaster.models.Movie;
import me.sudar.moviemaster.network.TmDbService;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MovieGridFragment extends Fragment {

    public static final String SAVED_MOVIE = "SAVED_MOVIE";
    private View view;
    private Activity activity;

    private MenuItem popMovieMenu;
    private MenuItem highRatedMenu;

    private MovieGridAdapter movieGridAdapter;

    public interface CallBacks{
        void onItemSelected(Movie movie);
    }

    public MovieGridFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_main, container, false);

        activity = getActivity();
        if(!(activity instanceof CallBacks))
            throw new IllegalStateException("Activity must implement fragment's CallBacks Interface.");
        CallBacks callBacks = (CallBacks) activity; //the activity should have implemented the CallBacks interface

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.movie_grid_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        int gridLayoutSpanCount = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), gridLayoutSpanCount);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        movieGridAdapter = new MovieGridAdapter(callBacks);
        mRecyclerView.setAdapter(movieGridAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState == null){
            loadPopularMovies();
//            Toast.makeText(this.getActivity(), "null savedInstanceState", Toast.LENGTH_SHORT).show();
        }else{
            List<Movie> list = savedInstanceState.getParcelableArrayList(SAVED_MOVIE);
            view.findViewById(R.id.movie_grid_recycler_view).setVisibility(View.VISIBLE);
            movieGridAdapter.updateData(list);
//            Toast.makeText(this.getActivity(),"" + list.get(1).getTitle(),Toast.LENGTH_SHORT).show();
            view.findViewById(R.id.progressBar).setVisibility(View.GONE);
            view.findViewById(R.id.errorView).setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_MOVIE, (ArrayList<Movie>) movieGridAdapter.getData());
    }

    public void loadPopularMovies(){
        view.findViewById(R.id.errorView).setVisibility(View.GONE);
        view.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        view.findViewById(R.id.movie_grid_recycler_view).setVisibility(View.INVISIBLE);
        TmDbService
                .getService()
                .getPopularMovies()
                .cache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiCallReply>() {
                    @Override
                    public void onCompleted() {
                        popMovieMenu.setEnabled(false);
                        highRatedMenu.setEnabled(true);
                        MovieMasterApplication.selectedMenu = 0;
                        view.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                        view.findViewById(R.id.movie_grid_recycler_view).setVisibility(View.VISIBLE);
                        activity.setTitle(getString(R.string.popular_movies));
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                        view.findViewById(R.id.movie_grid_recycler_view).setVisibility(View.INVISIBLE);
                        view.findViewById(R.id.errorView).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(ApiCallReply apiCallReply) {
                        movieGridAdapter.updateData(apiCallReply.getMovies());
                    }
                });
    }

    public void loadHighRatedMovies(){
        view.findViewById(R.id.errorView).setVisibility(View.GONE);
        view.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        view.findViewById(R.id.movie_grid_recycler_view).setVisibility(View.INVISIBLE);
        TmDbService
                .getService()
                .getHighRatedMovies()
                .cache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiCallReply>() {
                    @Override
                    public void onCompleted() {
                        popMovieMenu.setEnabled(true);
                        highRatedMenu.setEnabled(false);
                        MovieMasterApplication.selectedMenu = 1;
                        view.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                        view.findViewById(R.id.movie_grid_recycler_view).setVisibility(View.VISIBLE);
                        activity.setTitle(getString(R.string.high_rated_movies));
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                        view.findViewById(R.id.movie_grid_recycler_view).setVisibility(View.INVISIBLE);
                        view.findViewById(R.id.errorView).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(ApiCallReply apiCallReply) {
                        movieGridAdapter.updateData(apiCallReply.getMovies());
                    }
                });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        popMovieMenu = menu.findItem(R.id.action_pop_movies);
        highRatedMenu = menu.findItem(R.id.action_high_rated_movies);

        if(MovieMasterApplication.selectedMenu == 0) {
            popMovieMenu.setEnabled(false);
        }
        else if(MovieMasterApplication.selectedMenu == 1) {
            highRatedMenu.setEnabled(false);
        }
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
}