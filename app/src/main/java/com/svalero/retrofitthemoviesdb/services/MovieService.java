package com.svalero.retrofitthemoviesdb.services;

import com.svalero.retrofitthemoviesdb.json_mapper.Movie;
import com.svalero.retrofitthemoviesdb.json_mapper.MovieResponse;
import com.svalero.retrofitthemoviesdb.movies_api.MoviesAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieService {
    private MoviesAPI moviesAPI;

    public MovieService(MoviesAPI moviesAPI) {
        this.moviesAPI = moviesAPI;
    }

    public void obtenerPeliculasPopulares(final OnResponseListener<List<Movie>> listener) {
        String apiKey = "f9ff6231b99fc39571a4b5088360d660";
        String language = "es-ES";
        int page = 1;

        Call<MovieResponse> call = moviesAPI.getPopularMovies(apiKey, language, page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body().getResults());
                } else {
                    listener.onFailure(new Exception("Error en la respuesta"));
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    public void searchMovie(String apiKey, String language, String query, int page, final OnResponseListener<List<Movie>> listener) {
        Call<MovieResponse> call = moviesAPI.searchMovie(apiKey, language, query, page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body().getResults());
                } else {
                    listener.onFailure(new Exception("Error en la respuesta"));
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    public void getMovieDetails(int movieId, String apiKey, String language, final OnResponseListener<Movie> listener) {
        Call<Movie> call = moviesAPI.getMovieDetails(movieId, apiKey, language);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(new Exception("Error en la respuesta"));
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    public interface OnResponseListener<T> {
        void onSuccess(T result);
        void onFailure(Throwable t);
    }
}
