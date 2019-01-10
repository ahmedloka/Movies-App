package com.example.android.movieStage1.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.movieStage1.database.AppDatabase;

import java.util.List;

public class VoteViewModel extends AndroidViewModel {

    private final LiveData<List<String>> getMoviesVote;

    public VoteViewModel(@NonNull Application application) {
        super(application);

        AppDatabase database = AppDatabase.getsInstance(application);
        getMoviesVote = database.taskDao().getVote();

    }

    public LiveData<List<String>> getMoviesVote() {
        return getMoviesVote;
    }

}
