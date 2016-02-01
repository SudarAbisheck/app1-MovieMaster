package me.sudar.moviemaster;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class MovieGridFragment extends Fragment {

    public MovieGridFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_main, container, false);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.movie_grid_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        int gridLayoutSpanCount = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), gridLayoutSpanCount);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(MovieMasterApplication.movieGridAdapter);
        return view;
    }
}