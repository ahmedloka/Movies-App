package com.example.android.movieStage1.utilities;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.moviestage2.R;

import java.util.ArrayList;
import java.util.List;

public class FavouriteMoviesAdapter extends RecyclerView.Adapter<FavouriteMoviesAdapter.MovieViewHolder> {

    private static List<String> mFavouriteTitleMovies = new ArrayList<>();

    private final FavouriteLongClickHandler mLongClickHandler;
    private final FavouriteClickHandler mClickHandler;

    public interface FavouriteLongClickHandler {
        void onLongClick(int position);
    }

    public interface FavouriteClickHandler {
        void onClick(int position);
    }

    public static List<String> getmFavouriteTitleMovies() {
        return mFavouriteTitleMovies;
    }

    public FavouriteMoviesAdapter(List<String> mFavouriteTitleMovies, FavouriteLongClickHandler mLongClickHandler, FavouriteClickHandler mClickHandler) {
        FavouriteMoviesAdapter.mFavouriteTitleMovies = mFavouriteTitleMovies;
        this.mLongClickHandler = mLongClickHandler;
        this.mClickHandler = mClickHandler;
    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        int layout = R.layout.item_favourite_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout, parent, false);

        return new MovieViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, final int position) {
        try {

            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    holder.mIndexTextView.setText("# " + position);
                    if (mFavouriteTitleMovies.size() != 0 && mFavouriteTitleMovies != null) {
                        holder.mMovieTitleTextView.setText(mFavouriteTitleMovies.get(position).replace("[", "")
                                .replace("]", " ").replace(",", "").replace("\"", " "));
                        holder.getAdapterPosition();
                    }
                }
            });

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mFavouriteTitleMovies.size();
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        final TextView mIndexTextView;
        final TextView mMovieTitleTextView;

        MovieViewHolder(final View itemView) {
            super(itemView);

            mIndexTextView = itemView.findViewById(R.id.movie_id_tv);
            mMovieTitleTextView = itemView.findViewById(R.id.favourite_movie_tv);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }


        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            mLongClickHandler.onLongClick(position);
            notifyDataSetChanged();
            return true;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickHandler.onClick(position);
            notifyDataSetChanged();
        }
    }


}
