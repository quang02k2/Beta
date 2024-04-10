package com.example.BetaModel.controller;

import com.example.BetaModel.dtos.RoomDto;
import com.example.BetaModel.exceptions.ResourceNotFoundException;
import com.example.BetaModel.responses.ApiResponse;
import com.example.BetaModel.services.implement.RoomServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.Repository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/room")
public class RoomController {
    @Autowired
    private RoomServiceImpl roomService;

    @PostMapping("/create")
    public ResponseEntity<RoomDto> createRoom(@RequestBody RoomDto roomDto, @RequestParam Long cinemaId){
        try {
            RoomDto createRoomDto = this.roomService.createRoomDto(roomDto,cinemaId);
            return new ResponseEntity<RoomDto>( createRoomDto, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<RoomDto> updateRoomDtoResponseEntity(@RequestBody RoomDto roomDto, @RequestParam Long roomId){
        try {
            RoomDto updateRoomDto = this.roomService.updateRoomDto(roomDto, roomId);
            return new ResponseEntity<RoomDto>(updateRoomDto, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<RoomDto>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> delete(@RequestParam Long roomId){
        try {
            this.roomService.deleteRoom(roomId);
            return new ResponseEntity<ApiResponse>(new ApiResponse("Room is successfully", true), HttpStatus.OK);
        }catch (ResourceNotFoundException e){
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.NOT_FOUND);
        }
    }

}
