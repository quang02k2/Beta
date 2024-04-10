package com.example.BetaModel.services.iservices;

import com.example.BetaModel.dtos.MovieDto;
import com.example.BetaModel.model.Cinema;
import com.example.BetaModel.model.Movie;
import com.example.BetaModel.model.Room;

import java.util.List;

public interface MovieService {
    public MovieDto createMovie(MovieDto movieDto, Long movieTypeId, Long retaTd);

    public MovieDto updateMovie(MovieDto movieDto, Long movieId);

    void deleteMovie(Long movieId);

    public List<Movie> getPopularMovies(int limit);

    public List<Movie> getMoviesByCinema(Long cinemaId);

    public List<Movie> getMoviesByRoom(Long roomId);

}

