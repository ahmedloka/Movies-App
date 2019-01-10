package com.example.android.movieStage1.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.movieStage1.database.AppDatabase;

import java.util.List;

public class ReleaseDateViewModel extends AndroidViewModel {

    private final LiveData<List<String>> getMoviesRelease ;

    public ReleaseDateViewModel(@NonNull Application application) {
        super(application);

        AppDatabase database = AppDatabase.getsInstance(application);
        getMoviesRelease = database.taskDao().getReleaseDate();

    }

    public LiveData<List<String>> getMoviesRelease() {
        return getMoviesRelease;
    }

}
