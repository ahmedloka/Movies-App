package com.example.android.movieStage1;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.android.movieStage1.database.AppDatabase;
import com.example.android.movieStage1.viewmodel.OverviewViewModel;
import com.example.android.movieStage1.viewmodel.ReleaseDateViewModel;
import com.example.android.movieStage1.viewmodel.TitleViewModel;
import com.example.android.movieStage1.viewmodel.VoteViewModel;
import com.example.android.moviestage2.R;
import com.example.android.moviestage2.databinding.ActivityFavouriteDetailsMovieBinding;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.movieStage1.MainFavouriteMovies.EXTRA_POSITION;

public class FavouriteDetailsMovie extends AppCompatActivity {

    private static final String TAG = System.class.getSimpleName();
    private static final int DEF_VALUE = 0;
    public static final String EXTRA_REMOVE_MOVIE = "removeMovie";
    private static final String KEY_SCROLL_POSITION = "scrollPosition";

    private AppDatabase mDB;

    private LiveData<List<String>> listVote;
    private LiveData<List<String>> listDate;
    private LiveData<List<String>> listOverview;

    private final List<String> listVotee = new ArrayList<>();
    private final List<String> listDatee = new ArrayList<>();
    private final List<String> listOvervieww = new ArrayList<>();

    private int position;
    private ActivityFavouriteDetailsMovieBinding mDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_favourite_details_movie);

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mDB = AppDatabase.getsInstance(getApplicationContext());

        position = intent.getIntExtra(EXTRA_POSITION, DEF_VALUE);

        setUpUI(position);

        //for testing

        listDate = mDB.taskDao().getReleaseDate();
        listDate.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                Log.d(TAG, "onCreate: " + strings.get(position));
                listDatee.add(strings.toString());
            }
        });

        listOverview = mDB.taskDao().getOverview();
        listOverview.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                Log.d(TAG, "onCreate: " + strings.get(position));
                listOvervieww.add(strings.toString());
            }
        });


        listVote = mDB.taskDao().getVote();
        listVote.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                Log.d(TAG, "onCreate: " + strings.get(position));
                listVotee.add(strings.toString());
            }
        });

        //
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDataBinding.btnRmvFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainFavouriteMovies.class);
                i.putExtra(EXTRA_REMOVE_MOVIE, position);
                startActivity(i);
                finish();
            }
        });

    }


    private void setUpUI(final int position) {
        OverviewViewModel overviewViewModel = new OverviewViewModel(getApplication());
        overviewViewModel.getMoviesOverview().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                Log.d(TAG, "onChanged: " + strings);
                Log.d(TAG, "onChangeded: " + strings.get(position));
                mDataBinding.movieSynopsisTv.setText(strings.get(position).replace('[', ' ')
                        .replace(']', ' ').replace('"', ' '));
            }
        });
        TitleViewModel titleViewModel = new TitleViewModel(getApplication());
        titleViewModel.getGetMoviesTitle().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                Log.d(TAG, "onChanged: " + strings);
                Log.d(TAG, "onChangeded: " + strings.get(position));
                mDataBinding.movieTitleTv.setText(strings.get(position).replace('[', ' ')
                        .replace(']', ' ').replace('"', ' '));
            }
        });
        VoteViewModel voteViewModel = new VoteViewModel(getApplication());
        voteViewModel.getMoviesVote().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                Log.d(TAG, "onChanged: " + strings);
                Log.d(TAG, "onChangeded: " + strings.get(position));
                mDataBinding.rateTv.setText(strings.get(position).replace('[', ' ')
                        .replace(']', ' ').replace('"', ' '));
            }
        });
        ReleaseDateViewModel releaseDateViewModel = new ReleaseDateViewModel(getApplication());
        releaseDateViewModel.getMoviesRelease().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                Log.d(TAG, "onChanged: " + strings);
                Log.d(TAG, "onChangeded: " + strings.get(position));
                mDataBinding.releaseYearTv.setText(strings.get(position).replace('[', ' ')
                        .replace(']', ' ').replace('"', ' '));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id ==android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(),MainFavouriteMovies.class);
        startActivity(intent);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        final int [] scrollPosition = savedInstanceState.getIntArray(KEY_SCROLL_POSITION);
        if (scrollPosition == null){
            return;
        }
        mDataBinding.scrollView.post(new Runnable() {
            @Override
            public void run() {
                mDataBinding.scrollView.scrollTo(scrollPosition[0] , scrollPosition[1]);
            }
        });

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(KEY_SCROLL_POSITION, new int[]{mDataBinding.scrollView.getScrollX(), mDataBinding.scrollView.getScrollY()});

    }
}

