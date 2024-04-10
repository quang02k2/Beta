package com.example.BetaModel.services.implement;

import com.example.BetaModel.dtos.SeatDto;
import com.example.BetaModel.exceptions.ResourceNotFoundException;
import com.example.BetaModel.model.*;
import com.example.BetaModel.respository.*;
import com.example.BetaModel.services.iservices.SeatService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatServiceImpl implements SeatService {

    @Autowired
    private SeatRepo seatRepo;

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private SeatStatusRepo seatStatusRepo;

    @Autowired
    private SeatTypeRepo seatTypeRepo;

    @Autowired
    private BillTicketRepo billTicketRepo;

    @Autowired
    private TicketRepo ticketRepo;


    @Autowired
    private ModelMapper modelMapper;


    public List<Seat> getSeatsByRoom(Room room) {
        return seatRepo.findByRoom(room);
    }

    public List<Seat> getSeatsByRoom(Long roomId) {
        Room room = roomRepo.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room", "roomId", roomId));
        return getSeatsByRoom(room);
    }

    @Override
    public SeatDto createSeatDto(SeatDto seatDto, Long seatStatusId, Long seatTypeId, Long roomId) {
        SeatStatus seatStatus = this.seatStatusRepo.findById(seatStatusId).orElseThrow(()-> new ResourceNotFoundException("SeatStatus","seatStatusId", seatStatusId));
        SeatType seatType = this.seatTypeRepo.findById(seatTypeId).orElseThrow(()-> new ResourceNotFoundException("SeatType", "seatTypeId", seatTypeId));
        Room room = this.roomRepo.findById(roomId).orElseThrow(()-> new ResourceNotFoundException("room", "RoomId", roomId));

        Seat seat = this.DtoToSeat(seatDto);
        seat.setSeatStatus(seatStatus);
        seat.setSeatType(seatType);
        seat.setRoom(room);
        Seat createSeat = this.seatRepo.save(seat);

        return this.SeatToDto(createSeat);
    }

    @Override
    public SeatDto updateSeatDto(SeatDto seatDto, Long seatId) {
        Seat seat = this.seatRepo.findById(seatId).orElseThrow(()-> new ResourceNotFoundException("Seat", "SeatID", seatId));
        seat.setNumber(seatDto.getNumber());
        seat.setLine(seatDto.getLine());
        seat.setActive(seatDto.isActive());

        Seat updateSeat = this.seatRepo.save(seat);
        return this.SeatToDto(updateSeat);
    }

    @Override
    public void deleteSeat(Long seatId) {
        Seat seat = this.seatRepo.findById(seatId).orElseThrow(()-> new ResourceNotFoundException("Seat", "SeatID", seatId));
        for(BillTicket billTicket: billTicketRepo.findAll()){
            if(billTicket.getTicket().getSeat().getId() == seatId){
                billTicket.setBill(null);
                billTicket.setTicket(null);
                this.billTicketRepo.deleteById(billTicket.getId());
            }
        }

        for (Ticket ticket: ticketRepo.findAll()){
            if (ticket.getSeat().getId() == seatId){
                ticket.setSchedule(null);
                ticket.setSeat(null);
                this.ticketRepo.deleteById(ticket.getId());
            }
        }

        seat.setSeatStatus(null);
        seat.setSeatType(null);

        this.seatRepo.delete(seat);
    }

    public Seat DtoToSeat(SeatDto seatDto){
        return this.modelMapper.map(seatDto, Seat.class);
    }

    public SeatDto SeatToDto(Seat seat){
        return this.modelMapper.map(seat, SeatDto.class);
    }
}
