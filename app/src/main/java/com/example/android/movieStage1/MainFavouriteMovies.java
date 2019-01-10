package com.example.android.movieStage1;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.movieStage1.database.AppDatabase;
import com.example.android.movieStage1.utilities.FavouriteMoviesAdapter;
import com.example.android.movieStage1.viewmodel.TitleViewModel;
import com.example.android.movieStage1.viewmodel.VoteViewModel;
import com.example.android.moviestage2.R;
import com.example.android.moviestage2.databinding.ActivityFavouriteMoviesBinding;

import java.util.List;

import static com.example.android.movieStage1.FavouriteDetailsMovie.EXTRA_REMOVE_MOVIE;
import static com.example.android.movieStage1.MainActivity.LIST_STATE;

public class MainFavouriteMovies extends AppCompatActivity implements FavouriteMoviesAdapter.FavouriteLongClickHandler, FavouriteMoviesAdapter.FavouriteClickHandler {

    private static final String TAG = System.class.getSimpleName();

    public static final String EXTRA_POSITION = "extraPosition";


    private GridLayoutManager layoutManager;
    private FavouriteMoviesAdapter moviesAdapter;

    private AppDatabase mDB;

    private ActivityFavouriteMoviesBinding mDataBinding;

    private Parcelable listState;

    private LiveData<List<String>> listFavouriteMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_favourite_movies);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        layoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));
        mDB = AppDatabase.getsInstance(getApplicationContext());

        VoteViewModel voteViewModel = new VoteViewModel(getApplication());
        voteViewModel.getMoviesVote().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                Log.d(TAG, "onChanged: " + strings);
            }
        });

        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable(LIST_STATE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


        TitleViewModel viewModel = ViewModelProviders.of(this).get(TitleViewModel.class);

        viewModel.getGetMoviesTitle().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                moviesAdapter = new FavouriteMoviesAdapter(strings, MainFavouriteMovies.this, MainFavouriteMovies.this);
                mDataBinding.favouriteRv.setHasFixedSize(true);
                mDataBinding.favouriteRv.setLayoutManager(layoutManager);
                mDataBinding.favouriteRv.getLayoutManager().onRestoreInstanceState(listState);
                mDataBinding.favouriteRv.setAdapter(moviesAdapter);
            }
        });

        listFavouriteMovies = mDB.taskDao().getMoviesTitle();

        mDataBinding.ivBulb.setImageResource(R.drawable.ic_help_outline_black_36dp);
        mDataBinding.ivBulb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), R.string.remove_message, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_REMOVE_MOVIE)) {
            if (!(intent.getIntExtra(EXTRA_REMOVE_MOVIE, 0) == 0)) {
                int position = intent.getIntExtra(EXTRA_REMOVE_MOVIE, 0);
                this.onLongClick(position);
            }
        }
    }

    @Override
    public void onLongClick(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.delete_favourite)
                .setMessage(R.string.delete_favourite_message);

        builder.setPositiveButton(R.string.positive_action, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteMovieFromDB(position);
                Toast.makeText(MainFavouriteMovies.this, R.string.item_deleted_message, Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton(R.string.negative_action, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Intent intent = new Intent(getApplicationContext(), FavouriteDetailsMovie.class);
                intent.putExtra(EXTRA_POSITION, getIntent().getIntExtra(EXTRA_REMOVE_MOVIE, 0));

                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    private static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

    private void deleteMovieFromDB(final int position) {
        final List<String> movieList = FavouriteMoviesAdapter.getmFavouriteTitleMovies();

        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDB.taskDao().deleteTitleItem(movieList.get(position));
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(LIST_STATE, mDataBinding.favouriteRv.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onClick(int position) {
        final Intent intent = new Intent(this, FavouriteDetailsMovie.class);
        intent.putExtra(EXTRA_POSITION, position);
        startActivity(intent);
    }
}
