package com.svalero.retrofitthemoviesdb;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.svalero.retrofitthemoviesdb.json_mapper.Movie;
import com.svalero.retrofitthemoviesdb.movies_api.MoviesAPI;
import com.svalero.retrofitthemoviesdb.services.MovieService;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private MovieService movieService;
    private Button btnObtenerPeliculasPopulares;
    private Button btnBuscarPelicula;
    private Button btnObtenerDetallesPelicula;

    private EditText etBuscarPelicula; // Para capturar el término de búsqueda
    private EditText etMovieId; // Para capturar el ID de la película para detalles

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MoviesAPI moviesAPI = retrofit.create(MoviesAPI.class);
        movieService = new MovieService(moviesAPI);

        btnObtenerPeliculasPopulares = findViewById(R.id.btnObtenerPeliculasPopulares);
        btnBuscarPelicula = findViewById(R.id.btnBuscarPelicula);
        btnObtenerDetallesPelicula = findViewById(R.id.btnObtenerDetallesPelicula);


        etBuscarPelicula = findViewById(R.id.etBuscarPelicula); // EditText para el término de búsqueda
        etMovieId = findViewById(R.id.etMovieId); // EditText para el ID de la película

        btnObtenerPeliculasPopulares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieService.obtenerPeliculasPopulares(new MovieService.OnResponseListener<List<Movie>>() {
                    @Override
                    public void onSuccess(List<Movie> result) {
                        for (Movie movie : result) {
                            Toast.makeText(MainActivity.this, "Película: " + movie.getTitle(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnBuscarPelicula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = etBuscarPelicula.getText().toString().trim();
                if (!query.isEmpty()) {
                    buscarPelicula(query);
                } else {
                    Toast.makeText(MainActivity.this, "Por favor, ingresa un término de búsqueda", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnObtenerDetallesPelicula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movieId = etMovieId.getText().toString().trim();
                if (!movieId.isEmpty()) {
                    obtenerDetallesPelicula(Integer.parseInt(movieId));
                } else {
                    Toast.makeText(MainActivity.this, "Por favor, ingresa un ID de película", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void buscarPelicula(String query) {
        String apiKey = "f9ff6231b99fc39571a4b5088360d660";
        String language = "es-ES";
        int page = 1;

        movieService.searchMovie(apiKey, language, query, page, new MovieService.OnResponseListener<List<Movie>>() {
            @Override
            public void onSuccess(List<Movie> result) {
                for (Movie movie : result) {
                    Toast.makeText(MainActivity.this, "Película: " + movie.getTitle(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerDetallesPelicula(int movieId) {
        String apiKey = "f9ff6231b99fc39571a4b5088360d660";
        String language = "es-ES";

        movieService.getMovieDetails(movieId, apiKey, language, new MovieService.OnResponseListener<Movie>() {
            @Override
            public void onSuccess(Movie result) {
                Toast.makeText(MainActivity.this, "Título: " + result.getTitle() + "\nDescripción: " + result.getOverview(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
