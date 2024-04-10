package com.example.BetaModel.services.iservices;

import com.example.BetaModel.dtos.SeatDto;
import com.example.BetaModel.model.Room;
import com.example.BetaModel.model.Seat;

import java.util.List;

public interface SeatService {

    public List<Seat> getSeatsByRoom(Long roomId) ;
    public SeatDto createSeatDto(SeatDto seatDto, Long seatStatusId, Long seatTypeId, Long roomId);
    public SeatDto updateSeatDto(SeatDto seatDto, Long seatId);
    void deleteSeat(Long seatId);

}
