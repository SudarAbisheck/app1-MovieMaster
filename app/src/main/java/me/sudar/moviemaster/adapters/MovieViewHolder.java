package me.sudar.moviemaster.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import me.sudar.moviemaster.R;

/**
 * Created by sudar on 1/2/16.
 * Email : hey@sudar.me
 */
public class MovieViewHolder extends RecyclerView.ViewHolder {
    RelativeLayout gridItemCardView;
    ImageView moviePoster;

    public MovieViewHolder(View itemView) {
        super(itemView);
        gridItemCardView = (RelativeLayout) itemView.findViewById(R.id.grid_item);
        moviePoster = (ImageView) itemView.findViewById(R.id.movie_poster_image_view);
    }
}
