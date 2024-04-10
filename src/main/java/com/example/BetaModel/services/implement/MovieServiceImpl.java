package com.example.BetaModel.services.implement;

import com.example.BetaModel.dtos.MovieDto;
import com.example.BetaModel.exceptions.ResourceNotFoundException;
import com.example.BetaModel.model.*;
import com.example.BetaModel.respository.*;
import com.example.BetaModel.services.iservices.MovieService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Value("${project.image}")
    private String path;

    @Autowired
    private FileServiceImpl fileService;

    @Autowired
    private MovieRepo movieRepo;

    @Autowired
    private ScheduleRepo scheduleRepo;

    @Autowired
    private TicketRepo ticketRepo;

    @Autowired
    private BillTicketRepo billTicketRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MovieTypeRepo movieTypeRepo;

    @Autowired
    private RateRepo rateRepo;

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private CinemaRepo cinemaRepo;

    @Override
    public MovieDto createMovie(MovieDto movieDto, Long movieTypeId, Long retaTd) {
        MovieType movieType = this.movieTypeRepo.findById(movieTypeId).orElseThrow(()-> new ResourceNotFoundException("MovieType","MovieTypeId", movieTypeId));
        Rate rate = this.rateRepo.findById(retaTd).orElseThrow(()-> new ResourceNotFoundException("Rate", "RateId", retaTd));

        Movie movie = this.DtoToMovie(movieDto);
        movie.setMovieType(movieType);
        movie.setRate(rate);
        movie.setImage("default.png");
        movie.setHeroImage("default.png");

        Movie createMovie = this.movieRepo.save(movie);

        return this.MovieToDto(createMovie);
    }

    @Override
    public MovieDto updateMovie(MovieDto movieDto, Long movieId) {
        Movie movie = this.movieRepo.findById(movieId).orElseThrow(()-> new ResourceNotFoundException("Movie", "MovieId", movieId));
        movie.setEndTime(movieDto.getEndTime());
        movie.setPremiereDate(movieDto.getPremiereDate());
        movie.setDescription(movieDto.getDescription());
        movie.setDirector(movieDto.getDirector());
        movie.setImage(movieDto.getImage());
        movie.setHeroImage(movieDto.getHeroImage());
        movie.setMovieDuration(movieDto.getMovieDuration());
        movie.setLanguage(movieDto.getLanguage());
        movie.setName(movieDto.getName());
        movie.setTrailer(movieDto.getTrailer());
        movie.setActive(movie.isActive());

        Movie updateMovie = this.movieRepo.save(movie);
        return this.MovieToDto(updateMovie);
    }

    @Override
    public void deleteMovie(Long movieId) {
        // Find the movie by ID or throw an exception if not found
        Movie movie = this.movieRepo.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("movie", "movieId", movieId));

        // Delete related bill tickets
        for (BillTicket billTicket : billTicketRepo.findAll()) {
            if (billTicket.getTicket() != null
                    && billTicket.getTicket().getSchedule() != null
                    && billTicket.getTicket().getSchedule().getMovie() != null
                    && billTicket.getTicket().getSchedule().getMovie().getId().equals(movieId)) {
                billTicketRepo.deleteById(billTicket.getId());
            }
        }

        // Delete related tickets
        for (Ticket ticket : ticketRepo.findAll()) {
            if (ticket.getSchedule() != null
                    && ticket.getSchedule().getMovie() != null
                    && ticket.getSchedule().getMovie().getId().equals(movieId)) {
                ticketRepo.deleteById(ticket.getId());
            }
        }

        // Delete related schedules
        for (Schedule schedule : scheduleRepo.findAll()) {
            if (schedule.getMovie() != null
                    && schedule.getMovie().getId().equals(movieId)) {
                schedule.setMovie(null);
                scheduleRepo.deleteById(schedule.getId());
            }
        }

        movieRepo.delete(movie);
    }

    @Override
    public List<Movie> getPopularMovies(int limit) {
        return movieRepo.findTopPopularMovie(limit);
    }

    @Override
    public List<Movie> getMoviesByCinema(Long cinemaId) {
        Cinema cinema = getCinemaById(cinemaId);
        List<Room> rooms = roomRepo.findByCinema(cinema);
        List<Movie> movies = new ArrayList<>();

        for (Room room : rooms) {
            List<Movie> moviesInRoom = movieRepo.findBySchedule_Room_Cinema(cinema);
            movies.addAll(moviesInRoom);
        }

        return movies;
    }

    private Cinema getCinemaById(Long cinemaId) {
        Cinema cinema = cinemaRepo.findById(cinemaId).orElse(null);
        if (cinema == null) {
            throw new ResourceNotFoundException("Cinema not found","cinemaId", cinemaId);
        }
        return cinema;
    }
    @Override
    public List<Movie> getMoviesByRoom(Long roomId) {
        Room room = getRoomById(roomId);
        return movieRepo.findBySchedule_Room(room);
    }

    private Room getRoomById(Long roomId) {
        Room room = roomRepo.findById(roomId).orElse(null);
        if (room == null) {
            throw new ResourceNotFoundException("Room not found","roomId", roomId);
        }
        return room;
    }

    public MovieDto getMovie(Long movieId){
        Movie movie = this.movieRepo.findById(movieId).orElseThrow(()-> new ResourceNotFoundException("Movie", "movieId", movieId));
        return this.MovieToDto(movie);
    }

    public MovieDto uploadImage(MultipartFile image, Long movieId) throws IOException {
        MovieDto movieDto = this.getMovie(movieId);
        String fileName = fileService.uploadImage(path, image);
        movieDto.setImage(fileName);
        return updateMovie(movieDto, movieId);
    }

    public InputStream downloadImage(String imageName) throws IOException {
        return fileService.getResource(path, imageName);
    }

    public Movie DtoToMovie(MovieDto movieDto){
        return this.modelMapper.map(movieDto, Movie.class);
    }

    public MovieDto MovieToDto(Movie movie){
        return this.modelMapper.map(movie, MovieDto.class);
    }
}
