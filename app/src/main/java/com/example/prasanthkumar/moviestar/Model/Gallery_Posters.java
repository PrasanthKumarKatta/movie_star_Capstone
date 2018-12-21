package com.example.prasanthkumar.moviestar.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gallery_Posters
{
    @SerializedName("file_path")
    @Expose
    private String file_path;

    public Gallery_Posters(String file_path) {
        this.file_path = file_path;
    }

    public String getFile_path() {
        return "http://image.tmdb.org/t/p/w500" + file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }
}
