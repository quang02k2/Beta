package com.example.BetaModel.services.iservices;

import com.example.BetaModel.dtos.RoomDto;

public interface RoomService {
    public RoomDto createRoomDto(RoomDto roomDto, Long cinemaId);
    public RoomDto updateRoomDto(RoomDto roomDto, Long roomId);

    void deleteRoom(Long roomId);
}
