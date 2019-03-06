package com.example.prasanthkumar.moviestar.Model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class MoviesResponse implements Serializable {

    @Expose
    private String page;
    @Expose
    private String total_Results;
    @Expose
    private String total_pages;
    @Expose
    private List<Movie> results;

    public MoviesResponse(String page, String total_Results, String total_pages, List<Movie> results)
    {
        this.page = page;
        this.total_Results = total_Results;
        this.total_pages = total_pages;
        this.results = results;
    }

    public String getPage()
    {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotal_Results() {
        return total_Results;
    }

    public void setTotal_Results(String total_Results) {
        this.total_Results = total_Results;
    }

    public String getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(String total_pages) {
        this.total_pages = total_pages;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }
}
