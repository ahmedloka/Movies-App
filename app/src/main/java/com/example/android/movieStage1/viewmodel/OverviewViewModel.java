package com.example.android.movieStage1.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.movieStage1.database.AppDatabase;

import java.util.List;

public class OverviewViewModel extends AndroidViewModel {

    private final LiveData<List<String>> getMoviesOverview;

    public OverviewViewModel(@NonNull Application application) {
        super(application);

        AppDatabase database = AppDatabase.getsInstance(application);
        getMoviesOverview = database.taskDao().getOverview();

    }

    public LiveData<List<String>> getMoviesOverview() {
        return getMoviesOverview;
    }

}
