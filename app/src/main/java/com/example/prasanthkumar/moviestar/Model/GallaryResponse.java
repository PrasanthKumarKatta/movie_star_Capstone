package com.example.prasanthkumar.moviestar.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GallaryResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("backdrops")
    @Expose
    private List<Backdrop> backdrops = null;
    @SerializedName("posters")
    @Expose
    private List<Poster> posters = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Backdrop> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(List<Backdrop> backdrops) {
        this.backdrops = backdrops;
    }

    public List<Poster> getPosters() {
        return posters;
    }

    public void setPosters(List<Poster> posters) {
        this.posters = posters;
    }

}
