package com.example.BetaModel.controller;

import com.example.BetaModel.dtos.SeatDto;
import com.example.BetaModel.exceptions.ResourceNotFoundException;
import com.example.BetaModel.model.Seat;
import com.example.BetaModel.responses.ApiResponse;
import com.example.BetaModel.services.implement.SeatServiceImpl;
import com.example.BetaModel.services.iservices.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/seat")
public class SeatController {

    @Autowired
    private SeatServiceImpl seatService;


    @GetMapping("/seats")
    public List<Seat> getSeatsByRoom(@RequestParam Long roomId) {
        return seatService.getSeatsByRoom(roomId);
    }

    @PostMapping("/create")
    public ResponseEntity<SeatDto> createSeat(@RequestBody SeatDto seatDto, @RequestParam Long seatStatusId, @RequestParam Long seatTypeId, @RequestParam Long roomId){
        try {
            SeatDto createDto = this.seatService.createSeatDto(seatDto, seatStatusId, seatTypeId, roomId);
            return new ResponseEntity<SeatDto>(createDto, HttpStatus.CREATED);
        }catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<SeatDto> updateSeat(@RequestBody SeatDto seatDto, @RequestParam Long seatId){
        try {
            SeatDto updateSeatDto = this.seatService.updateSeatDto(seatDto, seatId);
            return new ResponseEntity<SeatDto>(updateSeatDto, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteSeat(@RequestParam Long seatId){
        try {
            this.seatService.deleteSeat(seatId);
            return new ResponseEntity<ApiResponse>(new ApiResponse("Seat is delete successfully", true), HttpStatus.OK);
        }catch (ResourceNotFoundException ex){
            return new ResponseEntity<>(new ApiResponse(ex.getMessage(), false), HttpStatus.NOT_FOUND);
        }
    }

}
