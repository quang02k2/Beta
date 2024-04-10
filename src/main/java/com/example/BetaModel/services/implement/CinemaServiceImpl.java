package com.example.BetaModel.services.implement;

import com.example.BetaModel.dtos.CinemaDto;
import com.example.BetaModel.exceptions.ResourceNotFoundException;
import com.example.BetaModel.model.*;
import com.example.BetaModel.respository.*;
import com.example.BetaModel.services.iservices.CinemaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CinemaServiceImpl implements CinemaService {

    @Autowired
    private CinemaRepo cinemaRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BillTicketRepo billTicketRepo;

    @Autowired
    private TicketRepo ticketRepo;

    @Autowired
    private SeatRepo seatRepo;

    @Autowired
    private ScheduleRepo scheduleRepo;

    @Autowired
    private RoomRepo roomRepo;

    @Override
    public CinemaDto createCinema(CinemaDto cinemaDto) {
        Cinema cinema = this.dtoToCinema(cinemaDto);
        Cinema createCinema = this.cinemaRepo.save(cinema);
        return this.cinemaToDto(createCinema);
    }

    @Override
    public CinemaDto updateCinemaDto(CinemaDto cinemaDto, Long cinemaId) {
        Cinema cinema = this.cinemaRepo.findById(cinemaId).orElseThrow(()-> new ResourceNotFoundException("Cinema", "cinemaId", cinemaId));
        cinema.setAddress(cinemaDto.getAddress());
        cinema.setDescription(cinemaDto.getDescription());
        cinema.setCode(cinemaDto.getCode());
        cinema.setNameOfCinema(cinemaDto.getNameOfCinema());
        cinema.setActive(cinemaDto.isActive());

        Cinema updateCinema = this.cinemaRepo.save(cinema);
        return this.cinemaToDto(updateCinema);
    }

    @Override
    public void deleteCinema(Long cinemaId) {
        Cinema cinema = this.cinemaRepo.findById(cinemaId).orElseThrow(()-> new ResourceNotFoundException("Cinema", "cinemaId", cinemaId));
        for(BillTicket billTicket: billTicketRepo.findAll()){
            if(billTicket.getTicket().getSchedule().getRoom().getCinema().getId() == cinemaId){
                billTicket.setBill(null);
                billTicket.setTicket(null);
                this.billTicketRepo.deleteById(billTicket.getId());
            }
        }

        for (Ticket ticket: ticketRepo.findAll()){
            if(ticket.getSchedule().getRoom().getCinema().getId() == cinemaId){
                ticket.setSchedule(null);
                ticket.setSeat(null);
                this.ticketRepo.deleteById(ticket.getId());
            }
        }

        for (Schedule schedule: scheduleRepo.findAll()){
            if(schedule.getRoom().getCinema().getId() == cinemaId){
                schedule.setMovie(null);
                schedule.setRoom(null);
                this.scheduleRepo.deleteById(schedule.getId());
            }
        }

        for (Seat seat: seatRepo.findAll()){
            if(seat.getRoom().getCinema().getId() == cinemaId){
                seat.setRoom(null);
                seat.setSeatStatus(null);
                seat.setSeatType(null);
                this.seatRepo.deleteById(seat.getId());
            }
        }

        for (Room room: roomRepo.findAll()){
            if(room.getCinema().getId() == cinemaId){
                room.setCinema(null);
                roomRepo.deleteById(room.getId());
            }
        }

        this.cinemaRepo.delete(cinema);
    }

    public Cinema dtoToCinema(CinemaDto cinemaDto){
        return this.modelMapper.map(cinemaDto, Cinema.class);
    }

    public CinemaDto cinemaToDto(Cinema cinema){
        return this.modelMapper.map(cinema, CinemaDto.class);
    }
}
