package com.example.BetaModel.services.implement;

import com.example.BetaModel.dtos.RoomDto;
import com.example.BetaModel.exceptions.ResourceNotFoundException;
import com.example.BetaModel.model.*;
import com.example.BetaModel.respository.*;
import com.example.BetaModel.services.iservices.RoomService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private BillTicketRepo billTicketRepo;

    @Autowired
    private TicketRepo ticketRepo;

    @Autowired
    private SeatRepo seatRepo;

    @Autowired
    private ScheduleRepo scheduleRepo;

    @Autowired
    private CinemaRepo cinemaRepo;

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public RoomDto createRoomDto(RoomDto roomDto, Long cinemaId) {
        Cinema cinema = this.cinemaRepo.findById(cinemaId).orElseThrow(()-> new ResourceNotFoundException("Cinema", "cinemaId", cinemaId));
        Room room = this.dtoToRoom(roomDto);
        room.setCinema(cinema);
        Room creatRoom = this.roomRepo.save(room);
        return this.roomToDto(creatRoom);
    }

    @Override
    public RoomDto updateRoomDto(RoomDto roomDto, Long roomId) {
        Room  room = this.roomRepo.findById(roomId).orElseThrow(()-> new ResourceNotFoundException("Room", "roomId", roomId));
        room.setCapacity(roomDto.getCapacity());
        room.setType(roomDto.getType());
        room.setDescription(roomDto.getDescription());
        room.setCode(roomDto.getCode());
        room.setName(roomDto.getName());

        Room updateRoom = this.roomRepo.save(room);
        return this.roomToDto(updateRoom);
    }

    @Override
    public void deleteRoom(Long roomId) {
        Room  room = this.roomRepo.findById(roomId).orElseThrow(()-> new ResourceNotFoundException("Room", "roomId", roomId));
        for(BillTicket billTicket: this.billTicketRepo.findAll()){
            if(billTicket.getTicket().getSchedule().getRoom().getId() == roomId){
                billTicket.setBill(null);
                billTicket.setTicket(null);
                this.billTicketRepo.deleteById(billTicket.getId());
            }
        }

        for (Ticket ticket: this.ticketRepo.findAll()){
            if(ticket.getSchedule().getRoom().getId() == roomId){
                ticket.setSeat(null);
                ticket.setSchedule(null);
                this.ticketRepo.deleteById(ticket.getId());
            }
        }

        for (Schedule schedule: scheduleRepo.findAll()){
            if(schedule.getRoom().getId() == roomId){
                schedule.setRoom(null);
                this.scheduleRepo.deleteById(schedule.getId());
            }
        }

        for (Seat seat: this.seatRepo.findAll()){
            if(seat.getRoom().getId() == roomId){
                seat.setRoom(null);
                this.seatRepo.deleteById(seat.getId());
            }
        }

        this.roomRepo.delete(room);

    }

    public Room dtoToRoom(RoomDto roomDto){
        return this.modelMapper.map(roomDto, Room.class);
    }

    public RoomDto roomToDto(Room room){
        return this.modelMapper.map(room, RoomDto.class);
    }

}
