package com.example.BetaModel.services.iservices;

import com.example.BetaModel.dtos.CinemaDto;
import lombok.Data;


public interface CinemaService {
    public CinemaDto createCinema(CinemaDto cinemaDto);
    public CinemaDto updateCinemaDto(CinemaDto cinemaDto, Long cinemaId);
    void deleteCinema(Long cinemaId);
}
