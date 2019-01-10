package com.example.android.movieStage1.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviestage2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailersAdapterUtils extends RecyclerView.Adapter<TrailersAdapterUtils.MovieViewHolder> {
    private final List<String> mVideoKey;

    public interface TrailerOnClickHandler {
        void onClick(int postion);
    }

    private final TrailerOnClickHandler mClickListener;

    public TrailersAdapterUtils(List<String> videoKeyList, TrailerOnClickHandler trailerOnClickHandler) {
        this.mClickListener = trailerOnClickHandler;
        this.mVideoKey = videoKeyList ;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layout_item_trailer = R.layout.item_recycle_view_trailers;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layout_item_trailer, parent, false);


        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Picasso.get().load(R.drawable.ic_play_arrow_black_24dp)
                .resize(70,70)
                .into(holder.mVideoIV);

        holder.mLabel.setText("Trailer " + ++position);
    }

    @Override
    public int getItemCount() {
        return mVideoKey.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView mVideoIV;
        final TextView mLabel;

        MovieViewHolder(View itemView) {
            super(itemView);

            mVideoIV = itemView.findViewById(R.id.video_image_view);
            mLabel = itemView.findViewById(R.id.trailerLabel);

            mVideoIV.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickListener.onClick(position);
        }
    }


}
