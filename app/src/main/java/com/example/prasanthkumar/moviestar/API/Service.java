package com.example.prasanthkumar.moviestar.API;

import com.example.prasanthkumar.moviestar.Model.CastResponse;
import com.example.prasanthkumar.moviestar.Model.GalleryResponse;
import com.example.prasanthkumar.moviestar.Model.MoviesResponse;
import com.example.prasanthkumar.moviestar.Model.ReviewResponse;
import com.example.prasanthkumar.moviestar.Model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("discover/movie")
    Call<MoviesResponse> getTeluguMovies(@Query("api_key") String apiKey, @Query("with_original_language") String language);

    @GET("discover/movie")
    Call<MoviesResponse> getEnglishMovies(@Query("api_key") String apiKey, @Query("with_original_language") String language);

    @GET("discover/movie")
    Call<MoviesResponse> getHindiMovies(@Query("api_key") String apiKey, @Query("with_original_language") String language);

    @GET("discover/movie")
    Call<MoviesResponse> getPanjabiMovies(@Query("api_key") String apiKey, @Query("with_original_language") String language);

    @GET("discover/movie")
    Call<MoviesResponse> getTamilMovies(@Query("api_key") String apiKey, @Query("with_original_language") String language);

    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getMovieTrailer(@Path("movie_id") int id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewResponse> getMovieReviews(@Path("movie_id") int id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/casts")
    Call<CastResponse> getMovieCasts(@Path("movie_id") int id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/images")
    Call<GalleryResponse> getMovieImages(@Path("movie_id") int id, @Query("api_key") String apiKey);
}
