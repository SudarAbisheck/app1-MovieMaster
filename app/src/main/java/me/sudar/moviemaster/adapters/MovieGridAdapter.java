package me.sudar.moviemaster.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.sudar.moviemaster.R;
import me.sudar.moviemaster.activities.DetailsActivity;
import me.sudar.moviemaster.activities.MainActivity;
import me.sudar.moviemaster.fragments.MovieGridFragment;
import me.sudar.moviemaster.models.Movie;
import me.sudar.moviemaster.network.TmDbService;

/**
 * Created by sudar on 1/2/16.
 * Email : hey@sudar.me
 */
public class MovieGridAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private List<Movie> movies;
    private MovieGridFragment.CallBacks callBacks;
    private boolean setSelectedItem;
    private int selectedItem;

    public MovieGridAdapter(MovieGridFragment.CallBacks callBacks){
        this.movies = new ArrayList<>();
        this.callBacks = callBacks;
        this.setSelectedItem = false;
        this.selectedItem = 0;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_layout,parent,false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        Picasso.with(holder.moviePoster.getContext())
                .load(TmDbService.IMAGE_BASE_URL + movies.get(position).getPosterPath())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.moviePoster);
        holder.gridItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBacks.onItemSelected(movies.get(holder.getAdapterPosition()));
                if(setSelectedItem) {
                    selectedItem = holder.getAdapterPosition();
                    notifyDataSetChanged();
                }
            }
        });
        if(setSelectedItem) {
            if (position == selectedItem)
                holder.gridItemCardView.setActivated(true);
            else
                holder.gridItemCardView.setActivated(false);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void updateData(List<Movie> movies){
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public void setSelectedItem(boolean option){
        setSelectedItem = option;
        notifyDataSetChanged();
    }

    public void changeSelectedItem(int position){
        selectedItem = position;
    }

    public int getSelectedItem(){
        return selectedItem;
    }

    public List<Movie> getData(){ return this.movies; }
}
