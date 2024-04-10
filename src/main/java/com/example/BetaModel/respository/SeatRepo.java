package com.example.BetaModel.respository;

import com.example.BetaModel.model.Room;
import com.example.BetaModel.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepo extends JpaRepository<Seat, Long> {
    List<Seat> findByRoom(Room room);

}
