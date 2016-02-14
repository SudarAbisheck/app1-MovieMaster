package me.sudar.moviemaster.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.sudar.moviemaster.R;
import me.sudar.moviemaster.models.Trailer;

/**
 * Created by sudar on 14/2/16.
 * Email : hey@sudar.me
 */
public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.TrailerViewHolder> {

    private ArrayList<Trailer> trailers;

    public TrailerListAdapter() {
        this.trailers = new ArrayList<>();
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item_layout, parent, false);
        return new TrailerViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {

        holder.trailerTitle.setText(trailers.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }


    public void updateData(ArrayList<Trailer> trailers){
        this.trailers.clear();
        this.trailers.addAll(trailers);
        notifyDataSetChanged();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView trailerTitle;
        private CardView trailerItemLayout;
        private Context context;

        private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

        public TrailerViewHolder(Context context, View itemView) {
            super(itemView);
            trailerTitle = (TextView) itemView.findViewById(R.id.trailer_title);
            trailerItemLayout = (CardView) itemView.findViewById(R.id.trailer_item_layout);
            this.context = context;
            trailerItemLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Trailer trailer = trailers.get(position);
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + trailer.getKey())));
        }
    }
}
