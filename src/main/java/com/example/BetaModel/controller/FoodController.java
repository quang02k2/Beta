package com.example.BetaModel.controller;

import com.example.BetaModel.dtos.FoodDto;
import com.example.BetaModel.dtos.MovieDto;
import com.example.BetaModel.exceptions.ResourceNotFoundException;
import com.example.BetaModel.responses.ApiResponse;
import com.example.BetaModel.services.implement.FoodServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("${api.prefix}/food")
public class FoodController {
    @Autowired
    private FoodServiceImpl foodService;


    @PostMapping("/create")
    public ResponseEntity<FoodDto> createFood(@RequestBody FoodDto foodDto){
        try {
            FoodDto createFoodDto = this.foodService.createFoodDto(foodDto);
            return new ResponseEntity<FoodDto>(createFoodDto, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<FoodDto> updateFood(@RequestBody FoodDto foodDto, @RequestParam Long foodId){
        try {
            FoodDto updateFoodDto = this.foodService.updateFoodDto(foodDto, foodId);
            return new ResponseEntity<FoodDto>(updateFoodDto, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteFood(@RequestParam Long foodId){
        try {
            this.foodService.deleteFood(foodId);
            return new ResponseEntity<ApiResponse>(new ApiResponse("Food is delete successfully", true), HttpStatus.OK);
        }catch (ResourceNotFoundException e){
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/image/upload")
    public ResponseEntity<FoodDto> uploadImage(@RequestParam("image") MultipartFile image,
                                                @RequestParam Long foodId) throws IOException {
        FoodDto updatedFoodDto = foodService.uploadImage(image, foodId);
        return ResponseEntity.ok(updatedFoodDto);
    }


}
