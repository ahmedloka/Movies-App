package com.example.android.movieStage1.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.movieStage1.database.AppDatabase;

import java.util.List;

public class TitleViewModel extends AndroidViewModel {

    private final LiveData<List<String>> getMoviesTitle;

    public TitleViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getsInstance(application);
        getMoviesTitle = database.taskDao().getMoviesTitle();
    }

    public LiveData<List<String>> getGetMoviesTitle() {
        return getMoviesTitle;
    }

}
