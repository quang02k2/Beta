package com.example.BetaModel.services.iservices;

import com.example.BetaModel.dtos.FoodDto;

public interface FoodService {
    public FoodDto createFoodDto(FoodDto foodDto);

    public FoodDto updateFoodDto(FoodDto foodDto, Long foodId);

    void deleteFood(Long foodId);
}

