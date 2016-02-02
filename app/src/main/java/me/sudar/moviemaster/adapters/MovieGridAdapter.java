package me.sudar.moviemaster.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.sudar.moviemaster.R;
import me.sudar.moviemaster.models.Movie;
import me.sudar.moviemaster.network.TmDbService;

/**
 * Created by sudar on 1/2/16.
 * Email : hey@sudar.me
 */
public class MovieGridAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    List<Movie> movies;

    public MovieGridAdapter(){
        this.movies = new ArrayList<>();
    }

    public MovieGridAdapter(List<Movie> movies){
        this.movies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_layout,parent,false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Picasso.with(holder.moviePoster.getContext())
                .load(TmDbService.IMAGE_BASE_URL + movies.get(position).getPosterPath())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.moviePoster);
        holder.gridItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Clicked !!", Toast.LENGTH_SHORT).show();
            }
        });
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
}
