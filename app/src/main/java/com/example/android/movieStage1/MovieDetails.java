package com.example.android.movieStage1;

import android.arch.lifecycle.Observer;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.android.movieStage1.database.AppDatabase;
import com.example.android.movieStage1.database.Movie;
import com.example.android.movieStage1.utilities.JsonUtils;
import com.example.android.movieStage1.utilities.NetworkUtils;
import com.example.android.movieStage1.utilities.TrailersAdapterUtils;
import com.example.android.movieStage1.viewmodel.TitleViewModel;
import com.example.android.movieStage1.utilities.SharedPreferencesUtils;
import com.example.android.moviestage2.R;
import com.example.android.moviestage2.databinding.ActivityMovieDetailsBinding;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.example.android.movieStage1.MainActivity.EXTRA_MOVIE_ID;
import static com.example.android.movieStage1.MainActivity.EXTRA_OVER_VIEW;
import static com.example.android.movieStage1.MainActivity.EXTRA_POSTER;
import static com.example.android.movieStage1.MainActivity.EXTRA_RATE;
import static com.example.android.movieStage1.MainActivity.EXTRA_RELEASE_YEAR;
import static com.example.android.movieStage1.MainActivity.EXTRA_TITLE;
import static com.example.android.movieStage1.MainActivity.LIST_STATE;

public class MovieDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, SharedPreferences.OnSharedPreferenceChangeListener, TrailersAdapterUtils.TrailerOnClickHandler, CompoundButton.OnCheckedChangeListener {

    private static final int ID_LOADER_ONE = 1;
    private static final int ID_LOADER_TWO = 2;
    private static final String KEY_CONTENT_KEY = "keyContentKey";
    private static final String KEY_VIDEO = "keyVideo";
    private static final int DEFAULT_VALUE = 1;
    private static final String KEY_SCROLL_POSITION = "keyScroll";

    private LinearLayoutManager linearLayoutManager;

    private static final List<String> mListVideoKey = new ArrayList<>();

    private static final String KEY_STATE = "state";

    private static final String TAG = System.class.getSimpleName();

    private static final List<String> mListFavouriteTitlesMovies = new ArrayList<>();
    private static final List<String> mListFavouriteTitlesMoviesInDB = new ArrayList<>();

    private static final List<String> mListFavouriteVoteMovies = new ArrayList<>();
    private static final List<String> mListFavouriteTVoteMoviesInDB = new ArrayList<>();


    private static final List<String> mListFavouriteOverviewMovies = new ArrayList<>();
    private static final List<String> mListFavouriteOverviewMoviesInDB = new ArrayList<>();


    private static final List<String> mListFavouriteReleaseMovies = new ArrayList<>();
    private static final List<String> mListFavouriteReleaseMoviesInDB = new ArrayList<>();

    private ActivityMovieDetailsBinding mDataBinding;

    private Parcelable listState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDataBinding.btnFavourite.setOnCheckedChangeListener(this);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        AppDatabase mDB = AppDatabase.getsInstance(getApplicationContext());

        getSupportLoaderManager().initLoader(ID_LOADER_ONE, null, this);


        setUpUi();
        videoKeySetUp();


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        if (savedInstanceState != null) {
            savedInstanceState.getStringArrayList(KEY_STATE);
            listState = savedInstanceState.getParcelable(LIST_STATE);
        }

        Log.d(TAG, "onCreate: " + SharedPreferencesUtils.getListMovies(this));

    }

    private void setUpUi() {

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }


        String title = intent.getStringExtra(EXTRA_TITLE);
        String releaseData = intent.getStringExtra(EXTRA_RELEASE_YEAR);
        String voteAverage = intent.getStringExtra(EXTRA_RATE);
        String overView = intent.getStringExtra(EXTRA_OVER_VIEW);
        String posterPath = intent.getStringExtra(EXTRA_POSTER);
        int movieId = intent.getIntExtra(EXTRA_MOVIE_ID, DEFAULT_VALUE);

        Log.d(TAG, "setUpUi: " + movieId);
        mDataBinding.movieTitleTv.setText(title);
        mDataBinding.releaseYearTv.setText(releaseData);
        mDataBinding.rateTv.setText(voteAverage);
        mDataBinding.movieSynopsisTv.setText(overView);

        Picasso.get()
                .load(posterPath)
                .error(R.drawable.ic_error_placeholder)
                .placeholder(R.drawable.ic_movie_placeholder)
                .resize(260, 320)
                .into(mDataBinding.moviePosterIv);


        URL url = NetworkUtils.buildURLForReviews(movieId);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_CONTENT_KEY, url.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        android.content.Loader<String> loader = new android.content.Loader<>(this);

        if (loader == null) {
            loaderManager.initLoader(ID_LOADER_ONE, bundle, this);
        } else {
            loaderManager.restartLoader(ID_LOADER_ONE, bundle, this);
        }
    }

    private void videoKeySetUp() {

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        int movieId = intent.getIntExtra(EXTRA_MOVIE_ID, DEFAULT_VALUE);

        URL url = NetworkUtils.buildURLForTrials(movieId);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_VIDEO, url.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = new Loader<>(this);

        if (loader == null) {
            loaderManager.initLoader(ID_LOADER_TWO, bundle, this);
        } else {
            loaderManager.restartLoader(ID_LOADER_TWO, bundle, this);
        }

    }

    private void shareVideo(String videoKey) throws ActivityNotFoundException {

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoKey));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, NetworkUtils.openTrialOnYouTube(videoKey));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_close) {
            finish();
        } else if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        } else if (id == R.id.favourite_movies) {
            startActivity(new Intent(MovieDetails.this, MainFavouriteMovies.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public android.support.v4.content.Loader<String> onCreateLoader(int id, final Bundle args) {
        if (id == ID_LOADER_ONE) {
            return new AsyncTaskLoader<String>(this) {
                String jsonData;

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    mDataBinding.progressBar.setVisibility(View.VISIBLE);
                    if (args == null) {
                        return;
                    }
                    if (jsonData != null) {
                        deliverResult(jsonData);
                    }
                    forceLoad();
                }

                @Override
                public String loadInBackground() {
                    String setUrl = args.getString(KEY_CONTENT_KEY);

                    if (setUrl == null) {
                        return null;
                    }
                    try {
                        URL url = new URL(setUrl);
                        return NetworkUtils.getResponseFromHttpUrl(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                public void deliverResult(String data) {
                    if (data == null) {
                        return;
                    }
                    jsonData = data;
                    super.deliverResult(data);
                }
            };
        } else if (id == ID_LOADER_TWO) {
            return new AsyncTaskLoader<String>(this) {
                String jsonData;

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    mDataBinding.progressBar.setVisibility(View.VISIBLE);
                    if (args == null) {
                        return;
                    }
                    if (jsonData != null) {
                        deliverResult(jsonData);
                    }
                    forceLoad();
                }

                @Override
                public String loadInBackground() {
                    String setUrl = args.getString(KEY_VIDEO);

                    if (setUrl == null) {
                        return null;
                    }
                    try {
                        URL url = new URL(setUrl);
                        return NetworkUtils.getResponseFromHttpUrl(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                public void deliverResult(String data) {
                    jsonData = data;
                    super.deliverResult(data);
                }
            };
        }
        return null;
    }

    @Override
    public void onLoadFinished
            (@NonNull android.support.v4.content.Loader<String> loader, String data) throws NullPointerException {
        try {

            int id = loader.getId();
            Movie movie;
            if (id == ID_LOADER_ONE) {
                mDataBinding.progressBar.setVisibility(View.INVISIBLE);
                if (data == null) {
                    return;
                }
                movie = JsonUtils.parseMovieJsonReviewStageTwo(data);

                Log.i(TAG, "Content: " + movie.getContent());


                if (movie.getContent().isEmpty() || movie.getContent().equals(" ") || movie.getContent().equals("") || movie.getContent() == null) {
                    Toast.makeText(this, getString(R.string.error_data), Toast.LENGTH_SHORT).show();
                } else {
                    mDataBinding.videoCotent.setText(movie.getContent());
                }
            } else if (id == ID_LOADER_TWO) {
                mDataBinding.progressBar.setVisibility(View.INVISIBLE);
                if (data == null) {
                    return;
                }
                movie = JsonUtils.parseMovieJsonVideoStageTwo(data);

                Log.i(TAG, "Video: " + movie.getKey());

                if (movie.getKey() == null || movie.getKey().isEmpty()) {
                } else {
                    mListVideoKey.addAll(movie.getKey());
                    Log.d(TAG, "onLoadFinished: " + mListVideoKey);

                    mDataBinding.recycleTrailers.setLayoutManager(linearLayoutManager);
                    mDataBinding.recycleTrailers.setHasFixedSize(true);
                    TrailersAdapterUtils trailersAdapterUtils = new TrailersAdapterUtils(mListVideoKey, this);
                    mDataBinding.recycleTrailers.getLayoutManager().onRestoreInstanceState(listState);
                    mDataBinding.recycleTrailers.setAdapter(trailersAdapterUtils);
                }
            }
        } catch (NullPointerException e) {
            Toast.makeText(this, getString(R.string.error_data), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<String> loader) {

    }

    @Override
    public void onClick(int position) {
        shareVideo(mListVideoKey.get(position));
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
        final String movieName = mDataBinding.movieTitleTv.getText().toString();
        final String voteAverage = mDataBinding.rateTv.getText().toString();
        final String overView = mDataBinding.movieSynopsisTv.getText().toString();
        final String releaseDate = mDataBinding.releaseYearTv.getText().toString();

        setUpPref(isChecked, movieName, voteAverage, overView, releaseDate);

    }

    private void setUpPref(boolean isChecked, final String movieName, final String vote, final String overView
            , final String releaseDate) {
        if (isChecked) {

            TitleViewModel viewModel = new TitleViewModel(getApplication());
            viewModel.getGetMoviesTitle().observe(this, new Observer<List<String>>() {
                @Override
                public void onChanged(@Nullable List<String> strings) {
                    SharedPreferencesUtils.saveListMovies(strings, getApplicationContext());
                }
            });

            if (SharedPreferencesUtils.getListMovies(this).contains(movieName)) {
                mDataBinding.btnFavourite.setChecked(false);
                mDataBinding.btnFavourite.setError("");
                Toast.makeText(this, R.string.error_favourite, Toast.LENGTH_SHORT).show();

                return;
            }

            mListFavouriteTitlesMovies.add(movieName);
            String lastIndexTitle = mListFavouriteTitlesMovies.get(mListFavouriteTitlesMovies.size() - 1);
            mListFavouriteTitlesMoviesInDB.add(lastIndexTitle);

            mListFavouriteVoteMovies.add(vote);
            String lastIndexVote = mListFavouriteVoteMovies.get(mListFavouriteVoteMovies.size() - 1);
            mListFavouriteTVoteMoviesInDB.add(lastIndexVote);

            mListFavouriteOverviewMovies.add(overView);
            String lastIndexOverview = mListFavouriteOverviewMovies.get(mListFavouriteOverviewMovies.size() - 1);
            mListFavouriteOverviewMoviesInDB.add(lastIndexOverview);

            mListFavouriteReleaseMovies.add(releaseDate);
            String lastIndexRelease = mListFavouriteReleaseMovies.get(mListFavouriteReleaseMovies.size() - 1);
            mListFavouriteReleaseMoviesInDB.add(lastIndexRelease);


            AppExecutor.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    Movie movie = new Movie(mListFavouriteTitlesMoviesInDB, mListFavouriteTVoteMoviesInDB
                            , mListFavouriteOverviewMoviesInDB, mListFavouriteReleaseMoviesInDB);

                    movie.setFavouriteTitles(Collections.singletonList(mListFavouriteTitlesMoviesInDB.get
                            (mListFavouriteTitlesMoviesInDB.size() - 1)));

                    movie.setFavouriteVoteAverage(Collections.singletonList(mListFavouriteTVoteMoviesInDB.get
                            (mListFavouriteTVoteMoviesInDB.size() - 1)));

                    movie.setFavouriteOverview(Collections.singletonList(mListFavouriteOverviewMoviesInDB.get
                            (mListFavouriteOverviewMoviesInDB.size() - 1)));

                    movie.setFavouriteReleaseDate(Collections.singletonList(mListFavouriteReleaseMoviesInDB.get
                            (mListFavouriteReleaseMoviesInDB.size() - 1)));


                    Log.d(TAG, "run details: " + Collections.singletonList(mListFavouriteTitlesMoviesInDB.get(mListFavouriteTitlesMoviesInDB.size() - 1)));
                    if (movie.getFavouriteTitles().contains(movieName)) {

                        AppDatabase.getsInstance(getApplicationContext()).taskDao().insetTask(movie);
                    }
                }

            });
            mDataBinding.btnFavourite.setTextOn(getString(R.string.action_add));
            mDataBinding.btnFavourite.setTextOff(getString(R.string.action_add));

            Toast.makeText(this, R.string.added_successfully, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onCheckedChanged: " + "add : " + mListFavouriteTitlesMoviesInDB);


        } else

        {

            mListFavouriteTitlesMoviesInDB.remove(movieName);
            mDataBinding.btnFavourite.setTextOff(getString(R.string.action_add));
            mDataBinding.btnFavourite.setTextOn(getString(R.string.action_add));
            Log.d(TAG, "onCheckedChanged: " + "remove : " + mListFavouriteTitlesMoviesInDB);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        final int[] scrollPosition = savedInstanceState.getIntArray(KEY_SCROLL_POSITION);
        if (scrollPosition == null) {
            return;
        }
        mDataBinding.scrollView.post(new Runnable() {
            @Override
            public void run() {
                mDataBinding.scrollView.scrollTo(scrollPosition[0], scrollPosition[1]);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(LIST_STATE, mDataBinding.recycleTrailers.getLayoutManager().onSaveInstanceState());
        outState.putIntArray(KEY_SCROLL_POSITION, new int[]{mDataBinding.scrollView.getScrollX(), mDataBinding.scrollView.getScrollY()});
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_STATE)) {

            SharedPreferencesUtils.getListMovies(getApplicationContext());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }
}
