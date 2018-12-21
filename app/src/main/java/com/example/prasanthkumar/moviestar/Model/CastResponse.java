package com.example.prasanthkumar.moviestar.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastResponse {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("cast")
    @Expose
    private List<Cast> cast;

    public CastResponse(int id, List<Cast> cast) {
        this.id = id;
        this.cast = cast;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }
}
