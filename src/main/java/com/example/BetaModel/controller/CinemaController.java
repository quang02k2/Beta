package com.example.BetaModel.controller;

import com.example.BetaModel.dtos.CinemaDto;
import com.example.BetaModel.exceptions.ResourceNotFoundException;
import com.example.BetaModel.responses.ApiResponse;
import com.example.BetaModel.services.implement.CinemaServiceImpl;
import com.example.BetaModel.services.iservices.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/cinema")
public class CinemaController {

    @Autowired
    private CinemaServiceImpl cinemaService;


    @PostMapping("/create")
    public ResponseEntity<CinemaDto> createCinema(@RequestBody CinemaDto cinemaDto){
        CinemaDto creatCinemaDto = this.cinemaService.createCinema(cinemaDto);
        return new ResponseEntity<CinemaDto>(creatCinemaDto, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<CinemaDto> updateCinema(@RequestBody CinemaDto cinemaDto, @RequestParam Long cinemaId) {
        try {
            CinemaDto updatedCinemaDto = this.cinemaService.updateCinemaDto(cinemaDto, cinemaId);
            return new ResponseEntity<>(updatedCinemaDto, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteCinema(@RequestParam Long cinemaId) {
        try {
            this.cinemaService.deleteCinema(cinemaId);
            return new ResponseEntity<>(new ApiResponse("Cinema is deleted successfully", true), HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiResponse(ex.getMessage(), false), HttpStatus.NOT_FOUND);
        }
    }


}
