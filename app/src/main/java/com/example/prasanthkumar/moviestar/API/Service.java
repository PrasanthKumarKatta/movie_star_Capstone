package com.example.prasanthkumar.moviestar.API;

import com.example.prasanthkumar.moviestar.Model.MoviesResponse;
import com.example.prasanthkumar.moviestar.Model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("discover/movie")
    Call<MoviesResponse> getTeluguMovies(@Query("api_key") String apiKey );

    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getMovieTrailer(@Path("movie_id") int id, @Query("api_key") String apiKey);

}
