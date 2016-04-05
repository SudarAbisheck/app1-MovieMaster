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
import me.sudar.moviemaster.database.FavMovieDB;
import me.sudar.moviemaster.models.ApiCallReply;
import me.sudar.moviemaster.models.Movie;
import me.sudar.moviemaster.network.TmDbService;
import me.sudar.moviemaster.views.AutoFitRecyclerView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MovieGridFragment extends Fragment {

    public static final String SAVED_MOVIE = "SAVED_MOVIE";
    public static final String SELECTED_ITEM = "SELECTED_ITEM";
    private View view;
    private Activity activity;

    private MenuItem popMovieMenu;
    private MenuItem highRatedMenu;
    private MenuItem favMovieMenu;

    private RecyclerView mRecyclerView;
    private MovieGridAdapter movieGridAdapter;
    private CallBacks callBacks = null;

    public interface CallBacks{
        void onItemSelected(Movie movie);
        void onListLoaded(Movie firstInTheList);
        void onLoadingFailed();
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
        callBacks = (CallBacks) activity; //the activity should have implemented the CallBacks interface

        mRecyclerView = (RecyclerView) view.findViewById(R.id.movie_grid_recycler_view);
        mRecyclerView.setHasFixedSize(true);
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
            int selectedItem = savedInstanceState.getInt(SELECTED_ITEM);
            view.findViewById(R.id.movie_grid_recycler_view).setVisibility(View.VISIBLE);
            movieGridAdapter.updateData(list);
            movieGridAdapter.changeSelectedItem(selectedItem);
//            Toast.makeText(this.getActivity(),"" + list.get(1).getTitle(),Toast.LENGTH_SHORT).show();
            view.findViewById(R.id.progressBar).setVisibility(View.GONE);
            view.findViewById(R.id.errorView).setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_MOVIE, (ArrayList<Movie>) movieGridAdapter.getData());
        outState.putInt(SELECTED_ITEM, movieGridAdapter.getSelectedItem());
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
                        favMovieMenu.setEnabled(true);
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
                        callBacks.onLoadingFailed();
                    }

                    @Override
                    public void onNext(ApiCallReply apiCallReply) {
                        movieGridAdapter.updateData(apiCallReply.getMovies());
                        movieGridAdapter.changeSelectedItem(0);
                        ((GridLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(0, 0);
                        callBacks.onListLoaded(apiCallReply.getMovies().get(0));
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
                        favMovieMenu.setEnabled(true);
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
                        callBacks.onLoadingFailed();
                    }

                    @Override
                    public void onNext(ApiCallReply apiCallReply) {
                        movieGridAdapter.updateData(apiCallReply.getMovies());
                        movieGridAdapter.changeSelectedItem(0);
                        ((GridLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(0, 0);
                        callBacks.onListLoaded(apiCallReply.getMovies().get(0));
                    }
                });
    }

    public void loadFavoriteMovies(){
        view.findViewById(R.id.errorView).setVisibility(View.GONE);
        view.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.movie_grid_recycler_view).setVisibility(View.VISIBLE);
        FavMovieDB db = new FavMovieDB(getContext());
        ArrayList<Movie> favMovies = db.getAllMovies();
        movieGridAdapter.updateData(favMovies);
        movieGridAdapter.changeSelectedItem(0);((GridLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(0, 0);
        callBacks.onListLoaded(favMovies.get(0));
        popMovieMenu.setEnabled(true);
        highRatedMenu.setEnabled(true);
        favMovieMenu.setEnabled(false);
    }

    public void setSelectedItem(boolean option){
        movieGridAdapter.setSelectedItem(option);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        popMovieMenu = menu.findItem(R.id.action_pop_movies);
        highRatedMenu = menu.findItem(R.id.action_high_rated_movies);
        favMovieMenu = menu.findItem(R.id.action_fav_movies);

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
        }else if(id == R.id.action_fav_movies){
            loadFavoriteMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}