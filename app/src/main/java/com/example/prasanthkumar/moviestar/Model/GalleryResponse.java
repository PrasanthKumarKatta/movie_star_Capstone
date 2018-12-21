package com.example.prasanthkumar.moviestar.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GalleryResponse
{
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("posters")
    @Expose
    private List<Gallery_Posters> posters;

    public GalleryResponse(int id, List<Gallery_Posters> posters)
    {
        this.id = id;
        this.posters = posters;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Gallery_Posters> getPosters() {
        return posters;
    }

    public void setPosters(List<Gallery_Posters> posters) {
        this.posters = posters;
    }
}
