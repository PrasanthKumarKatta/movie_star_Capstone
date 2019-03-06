package com.example.prasanthkumar.moviestar.Model;

import android.content.Context;

import com.example.prasanthkumar.moviestar.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cast {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("profile_path")
    @Expose
    private String profile_link;

    @SerializedName("character")
    @Expose
    private String character;

    public Cast(String name, String profile_link, String character) {
        this.name = name;
        this.profile_link = profile_link;
        this.character = character;
    }

    public String getName() {
        return  "Name: " + name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_link() {
        return "http://image.tmdb.org/t/p/w500" + profile_link;
    }

    public void setProfile_link(String profile_link) {
        this.profile_link = profile_link;
    }

    public String getCharacter() {
        return "Charater: "+character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }
}
