package com.example.BetaModel.respository;

import com.example.BetaModel.model.Cinema;
import com.example.BetaModel.model.Movie;
import com.example.BetaModel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MovieRepo extends JpaRepository<Movie, Long> {
    @Query("SELECT m FROM Movie m JOIN m.schedule s JOIN s.ticket t GROUP BY m ORDER BY COUNT(t) DESC")
    List<Movie> findPopularMovie();

    List<Movie> findBySchedule_Room_Cinema(Cinema cinema);
    List<Movie> findBySchedule_Room(Room room);

    @Query("SELECT m FROM Movie m JOIN m.schedule s JOIN s.ticket t GROUP BY m ORDER BY COUNT(t) DESC")
    List<Movie> findTopPopularMovie(int limit);
}
