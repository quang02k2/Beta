package com.example.BetaModel.controller;

import com.example.BetaModel.dtos.MovieDto;
import com.example.BetaModel.exceptions.ResourceNotFoundException;
import com.example.BetaModel.model.Movie;
import com.example.BetaModel.responses.ApiResponse;
import com.example.BetaModel.services.implement.FileServiceImpl;
import com.example.BetaModel.services.implement.MovieServiceImpl;
import com.example.BetaModel.services.iservices.MovieService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/movie")
public class MovieController {
    @Autowired
    private MovieServiceImpl movieService;

    @Value("${project.image}")
    private String path;

    @Autowired
    private FileServiceImpl fileService;


    @PostMapping("/create")
    public ResponseEntity<MovieDto> createMovie(@RequestBody MovieDto movieDto, @RequestParam Long movieTypeId,@RequestParam Long retaId){
        MovieDto createMovieDto = this.movieService.createMovie(movieDto,movieTypeId,retaId);
        return new ResponseEntity<MovieDto>(createMovieDto, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<MovieDto> updateMovie(@RequestBody MovieDto movieDto, @RequestParam Long movieId){
        MovieDto updateMovieDto = this.movieService.updateMovie(movieDto,movieId);
        return new ResponseEntity<MovieDto>(updateMovieDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteMovie(@RequestParam Long movieId){
        try {
            this.movieService.deleteMovie(movieId);
            return new ResponseEntity<ApiResponse>(new ApiResponse("Movie is deleted successfully", true), HttpStatus.OK);
        }catch (ResourceNotFoundException e){
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.NOT_FOUND);
        }

    }


    @GetMapping("/popularMovies")
    public ResponseEntity<List<Movie>> getPopularMovies(@RequestParam int limit) {
        List<Movie> movies = movieService.getPopularMovies(limit);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/byCinema")
    public ResponseEntity<List<Movie>> getMoviesByCinema(@RequestParam Long cinemaId) {
        List<Movie> movies = movieService.getMoviesByCinema(cinemaId);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/byRoom")
    public ResponseEntity<List<Movie>> getMoviesByRoom(@RequestParam Long roomId) {
        List<Movie> movies = movieService.getMoviesByRoom(roomId);
        return ResponseEntity.ok(movies);
    }


    @PostMapping("/image/upload")
    public ResponseEntity<MovieDto> uploadImage(@RequestParam("image") MultipartFile image,
                                                 @RequestParam Long movieId) throws IOException {
        MovieDto updatedCourse = movieService.uploadImage(image, movieId);
        return ResponseEntity.ok(updatedCourse);
    }

    @GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@RequestParam String imageName, HttpServletResponse response) throws IOException {
        InputStream imageStream = movieService.downloadImage(imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(imageStream, response.getOutputStream());
    }

}
