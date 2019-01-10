package com.example.android.movieStage1.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "movie")
public class Movie {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "COL_ID")
    private int id;

    @ColumnInfo(name = "COL_TITLE")
    private List<String> favouriteTitles;

    @ColumnInfo(name = "COL_VOTE")
    private List<String> favouriteVoteAverage;

    @ColumnInfo(name = "COL_OVERVIEW")
    private List<String> favouriteOverview;

    @ColumnInfo(name = "COL_RELEASE_DATE")
    private List<String> favouriteReleaseDate;

    private List<String> voteAverage;

    private List<String> overView;

    private List<String> releaseData;

    //@ColumnInfo(name = "COL_TITLE")
    private List<String> title;

    private List<String> positionPath;

    private List<String> movieID;

    private List<String> key;

    private String content;

    public int getId() {
        return id;
    }

    public Movie(int id, List<String> favouriteTitles, List<String> favouriteVoteAverage, List<String> favouriteOverview, List<String> favouriteReleaseDate) {
        this.id = id;
        this.favouriteTitles = favouriteTitles;
        this.favouriteVoteAverage = favouriteVoteAverage;
        this.favouriteOverview = favouriteOverview;
        this.favouriteReleaseDate = favouriteReleaseDate;
    }

    @Ignore
    public Movie(List<String> movieID, List<String> voteAverage, List<String> title, List<String> positionPath, List<String> overView, List<String> releaseData) {
        this.movieID = movieID;
        this.voteAverage = voteAverage;
        this.title = title;
        this.positionPath = positionPath;
        this.overView = overView;
        this.releaseData = releaseData;
    }

    @Ignore
    public Movie() {

    }

    @Ignore
    public Movie(List<String> favouriteTitles, List<String> voteAverage, List<String> overView, List<String> releaseData) {

    }


    public List<String> getFavouriteVoteAverage() {
        return favouriteVoteAverage;
    }

    public void setFavouriteVoteAverage(List<String> favouriteVoteAverage) {
        this.favouriteVoteAverage = favouriteVoteAverage;
    }

    public List<String> getFavouriteOverview() {
        return favouriteOverview;
    }

    public void setFavouriteOverview(List<String> favouriteOverview) {
        this.favouriteOverview = favouriteOverview;
    }

    public List<String> getFavouriteReleaseDate() {
        return favouriteReleaseDate;
    }

    public void setFavouriteReleaseDate(List<String> favouriteReleaseDate) {
        this.favouriteReleaseDate = favouriteReleaseDate;
    }

    public List<String> getFavouriteTitles() {
        return favouriteTitles;
    }

    public void setFavouriteTitles(List<String> favouriteTitles) {
        this.favouriteTitles = favouriteTitles;
    }

    public List<String> getKey() {
        return key;
    }

    public void setKey(List<String> key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getVoteAverage() {
        return voteAverage;
    }

    public List<String> getMovieID() {
        return movieID;
    }

    public void setMovieID(List<String> movieID) {
        this.movieID = movieID;
    }

    public void setVoteAverage(List<String> voteAverage) {
        this.voteAverage = voteAverage;
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public List<String> getPositionPath() {
        return positionPath;
    }

    public void setPositionPath(List<String> positionPath) {
        this.positionPath = positionPath;
    }

    public List<String> getOverView() {
        return overView;
    }

    public void setOverView(List<String> overView) {
        this.overView = overView;
    }

    public List<String> getReleaseData() {
        return releaseData;
    }

    public void setReleaseData(List<String> releaseData) {
        this.releaseData = releaseData;
    }

}
