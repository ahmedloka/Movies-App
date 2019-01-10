package com.example.android.movieStage1.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.moviestage2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainMovieAdapterUtils extends RecyclerView.Adapter<MainMovieAdapterUtils.MovieViewHolder> {
    private final List<String> mData;

    public interface MoviesOnClickHandler {
        //TODO 1- I will do it , Do not forget your DREAM (ANDROID DEVELOPER)
        void onClick(int position);
    }

    private final MoviesOnClickHandler mClickHandler;

    public MainMovieAdapterUtils(List<String> mData, MoviesOnClickHandler mClickHandler) {
        this.mData = mData;
        this.mClickHandler = mClickHandler;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutItemGridView = R.layout.item_grid_view;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutItemGridView, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Picasso.get()
                .load(mData.get(position))
                .placeholder(R.drawable.ic_movie_placeholder)
                .error(R.drawable.ic_error_placeholder)
                .resize(360, 420)
                .into(holder.mItemImageView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView mItemImageView;

        MovieViewHolder(View itemView) {
            super(itemView);

            mItemImageView = itemView.findViewById(R.id.item_grid_view);
            mItemImageView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickHandler.onClick(position);
        }
    }
}
