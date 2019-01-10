package com.example.android.movieStage1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

//import com.example.android.movieStage1.Database.AppDatabase;
import com.example.android.movieStage1.database.Movie;
import com.example.android.movieStage1.utilities.JsonUtils;
import com.example.android.movieStage1.utilities.MainMovieAdapterUtils;
import com.example.android.movieStage1.utilities.NetworkUtils;
import com.example.android.moviestage2.R;
import com.example.android.moviestage2.databinding.ActivityMainBinding;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, MainMovieAdapterUtils.MoviesOnClickHandler {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_RELEASE_YEAR = "year";
    public static final String EXTRA_RATE = "rate";
    public static final String EXTRA_OVER_VIEW = "overView";
    public static final String EXTRA_POSTER = "poster";
    public static final String EXTRA_MOVIE_ID = "movieId";

    private static final String KEY_JSON_DATA = "loaderID";

    private static final String TAG = System.class.getSimpleName();
    private static final int ID_LOADER_ONE = 1;
    private static final int ID_LOADER_TWO = 2;
    public static final String LIST_STATE = "ListState";

    static final List<String> jsonPopularResponseData = new ArrayList<>();
    static final List<String> jsonMostRatedResponseData = new ArrayList<>();

    private Movie movie;
    private ActivityMainBinding mDataBinding;

    private Parcelable listState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        showRecViewPopular();

        getSupportLoaderManager().initLoader(ID_LOADER_ONE, null, this);
        getSupportLoaderManager().initLoader(ID_LOADER_TWO, null, this);

        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable(LIST_STATE);
        }

    }

    private void showRecViewPopular() {

        jsonPopularResponseData.clear();
        if (isNetworkAvailable(getApplicationContext())) {

            URL url = NetworkUtils.buildURLForPopularMovies();

            Bundle bundle = new Bundle();
            bundle.putString(KEY_JSON_DATA, url.toString());

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> loader = new Loader<>(this);

            if (loader != null) {
                loaderManager.initLoader(ID_LOADER_ONE, bundle, this);
            } else {
                loaderManager.restartLoader(ID_LOADER_ONE, bundle, this);
            }

            mDataBinding.moviesRecyclerView.setVisibility(View.VISIBLE);
            mDataBinding.errorTextView.setVisibility(View.INVISIBLE);
            return;
        }
        mDataBinding.moviesRecyclerView.setVisibility(View.INVISIBLE);
        mDataBinding.errorTextView.setVisibility(View.VISIBLE);
        Toast.makeText(this, getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
    }

    private void showRecViewMostRated() {

        jsonMostRatedResponseData.clear();
        if (isNetworkAvailable(getApplicationContext())) {

            URL url = NetworkUtils.buildURLForMostRated();

            if (url == null) {
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putString(KEY_JSON_DATA, url.toString());

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> loader = new Loader<>(this);

            if (loader == null) {
                loaderManager.initLoader(ID_LOADER_TWO, bundle, this);
            } else {
                loaderManager.restartLoader(ID_LOADER_TWO, bundle, this);
            }

            mDataBinding.moviesRecyclerView.setVisibility(View.VISIBLE);
            mDataBinding.errorTextView.setVisibility(View.INVISIBLE);
            return;
        }
        mDataBinding.moviesRecyclerView.setVisibility(View.INVISIBLE);
        mDataBinding.errorTextView.setVisibility(View.VISIBLE);
        Toast.makeText(this, getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_by, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.item_sort_by_most_rated) {
            jsonMostRatedResponseData.clear();
            showRecViewMostRated();
        } else if (id == R.id.item_sort_by_popular) {
            jsonPopularResponseData.clear();
            showRecViewPopular();
        } else if (id == R.id.favourite_movies) {
            startActivity(new Intent(MainActivity.this, MainFavouriteMovies.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        if (id == ID_LOADER_ONE) {
            return new AsyncTaskLoader<String>(this) {
                String jsonData;

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();

                    if (args == null) {
                        return;
                    }
                    deliverResult(jsonData);
                    forceLoad();
                    mDataBinding.progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public String loadInBackground() {
                    String setUrl = args.getString(KEY_JSON_DATA);

                    URL url;
                    String getResponseFromUrl = null;

                    try {
                        url = new URL(setUrl);
                        getResponseFromUrl = NetworkUtils.getResponseFromHttpUrl(url);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return getResponseFromUrl;
                }

                @Override
                public void deliverResult(@Nullable String data) {
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

                    if (args == null) {
                        return;
                    }
                    if (jsonData != null) {
                        deliverResult(jsonData);
                    }
                    forceLoad();
                    mDataBinding.progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public String loadInBackground() {
                    String setUrl = args.getString(KEY_JSON_DATA);

                    try {
                        URL url = new URL(setUrl);
                        return NetworkUtils.getResponseFromHttpUrl(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                public void deliverResult(@Nullable String data) {
                    jsonData = data;
                    super.deliverResult(data);
                }
            };
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        int id = loader.getId();
        if (id == ID_LOADER_ONE) {
            mDataBinding.progressBar.setVisibility(View.INVISIBLE);
            if (data == null) {
                return;
            }

            try {
                movie = JsonUtils.parseMovieJsonStageOne(data);

                Log.i(TAG, "MOVIE_TITLES_POPULAR: " + movie.getTitle());

                jsonPopularResponseData.addAll(movie.getPositionPath());
                mDataBinding.moviesRecyclerView.setHasFixedSize(true);

                mDataBinding.moviesRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(
                ), calculateNoOfColumns(this)));
                mDataBinding.moviesRecyclerView.getLayoutManager().onRestoreInstanceState(listState);

                MainMovieAdapterUtils mMoviesAdapter = new MainMovieAdapterUtils(jsonPopularResponseData, this);
                mDataBinding.moviesRecyclerView.setAdapter(mMoviesAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (id == ID_LOADER_TWO) {
            mDataBinding.progressBar.setVisibility(View.INVISIBLE);

            if (data == null) {
                return;
            }

            try {
                movie = JsonUtils.parseMovieJsonStageOne(data);

                Log.i(TAG, "MOVIE_TITLES_MOST_RATED: " + movie.getTitle());

                jsonMostRatedResponseData.addAll(movie.getPositionPath());
                mDataBinding.moviesRecyclerView.setHasFixedSize(true);

                mDataBinding.moviesRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext()
                        , calculateNoOfColumns(this)));

                mDataBinding.moviesRecyclerView.getLayoutManager().onRestoreInstanceState(listState);

                MainMovieAdapterUtils mMoviesAdapter = new MainMovieAdapterUtils(jsonMostRatedResponseData, this);
                mDataBinding.moviesRecyclerView.setAdapter(mMoviesAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    @Override
    public void onClick(int position) {
        try {

            Intent intent = new Intent(getApplicationContext(), MovieDetails.class);

            int movieId = Integer.valueOf(movie.getMovieID().get(position));
            String title = movie.getTitle().get(position);
            String year = movie.getReleaseData().get(position);
            String rate = String.valueOf(movie.getVoteAverage().get(position));
            String overView = movie.getOverView().get(position);
            String posterPath = movie.getPositionPath().get(position);

            intent.putExtra(EXTRA_MOVIE_ID, movieId);
            intent.putExtra(EXTRA_TITLE, title);
            intent.putExtra(EXTRA_RELEASE_YEAR, year);
            intent.putExtra(EXTRA_RATE, rate);
            intent.putExtra(EXTRA_OVER_VIEW, overView);
            intent.putExtra(EXTRA_POSTER, posterPath);


            startActivity(intent);

            Log.d(TAG, "TITLE: " + movie.getTitle());

        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(MainActivity.this, R.string.main_error_data, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (outState == null) {
            return;
        }
        try {
            outState.putParcelable(LIST_STATE, mDataBinding.moviesRecyclerView.getLayoutManager().onSaveInstanceState());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            jsonPopularResponseData.clear();
            jsonMostRatedResponseData.clear();
        } else {
            jsonPopularResponseData.clear();
            jsonMostRatedResponseData.clear();        }
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

    private boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

    }
}
