package com.example.prasanthkumar.moviestar.Model;

import android.database.Cursor;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Movie  {

    @SerializedName("original_title")
    @Expose
    private String original_title;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("backdrop_path")
    @Expose
    private String backdrop_path;

//    private int mvote_count;


    public Movie(String original_title, String posterPath,
                 String overview, Double voteAverage,
                 String releaseDate, Integer id,
                 String backdrop_path)
    {
        this.original_title = original_title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.id = id;
        this.backdrop_path = backdrop_path;
    }

    public Movie() {

    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getPosterPath() {
        return  posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBackdrop_path() {
        return  backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

  /*  public MovieInfo(Cursor cursor) {

        this.id = cursor.getInt(1);
        this.mvote_count = 0;
        this.mvideo = null;
        this.mvote_average = cursor.getString(2);
        this.mtitle = cursor.getString(3);
        this.mpopularity = null;
        this.mposter_path = cursor.getString(4);
        Log.i("posterpath", cursor.getString(4));
        this.moriginal_language = null;
        this.moriginal_title = cursor.getString(5);
        this.mbackdrop_path = cursor.getString(6);
        this.madult = null;
        this.moverview = cursor.getString(7);
        this.mrelease_date = cursor.getString(8);

    }*/
}
