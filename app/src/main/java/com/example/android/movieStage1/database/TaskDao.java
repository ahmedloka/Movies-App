package com.example.android.movieStage1.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT COL_TITLE FROM movie ORDER BY COL_ID")
    LiveData<List<String>> getMoviesTitle();

    @Query("SELECT COL_VOTE FROM movie ORDER BY COL_ID")
    LiveData<List<String>> getVote();

    @Query("SELECT COL_OVERVIEW FROM movie ORDER BY COL_ID")
    LiveData<List<String>> getOverview();

    @Query("SELECT COL_RELEASE_DATE FROM movie ORDER BY COL_ID")
    LiveData<List<String>> getReleaseDate();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insetTask(Movie movie);

    @Query("DELETE FROM movie WHERE COL_TITLE = :title")
    void deleteTitleItem(String title);

    @Update
    void updateTask(Movie movie);
}

