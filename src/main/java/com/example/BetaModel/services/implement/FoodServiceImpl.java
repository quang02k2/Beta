package com.example.BetaModel.services.implement;

import com.example.BetaModel.dtos.FoodDto;
import com.example.BetaModel.dtos.MovieDto;
import com.example.BetaModel.exceptions.ResourceNotFoundException;
import com.example.BetaModel.model.BillFood;
import com.example.BetaModel.model.Food;
import com.example.BetaModel.model.Movie;
import com.example.BetaModel.respository.BillFoodRepo;
import com.example.BetaModel.respository.FoodRepo;
import com.example.BetaModel.services.iservices.FoodService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FoodServiceImpl implements FoodService {

    @Value("${project.image}")
    private String path;


    @Autowired
    private FoodRepo foodRepo;

    @Autowired
    private BillFoodRepo billFoodRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileServiceImpl fileService;

    @Override
    public FoodDto createFoodDto(FoodDto foodDto) {
        Food food = this.DtoToFood(foodDto);
        food.setImage("default.png");

        Food createFood= this.foodRepo.save(food);

        return this.foodToDto(createFood);
    }

    @Override
    public FoodDto updateFoodDto(FoodDto foodDto, Long foodId) {
        Food food = this.foodRepo.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("Food", "FoodId", foodId));
        food.setPrice(foodDto.getPrice());
        food.setImage(foodDto.getImage());
        food.setDescription(foodDto.getDescription());
        food.setNameOfFood(foodDto.getNameOfFood());
        food.setActive(food.isActive());

        Food updateFood = this.foodRepo.save(food);
        return this.foodToDto(updateFood);
    }

    @Override
    public void deleteFood(Long foodId) {
        Food food = this.foodRepo.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("Food", "FoodId", foodId));
        for(BillFood billFood: billFoodRepo.findAll()){
            if (billFood.getFood().getId() == foodId){
                billFood.setFood(null);
                billFood.setBill(null);
                this.billFoodRepo.deleteById(billFood.getId());
            }
        }
        this.foodRepo.delete(food);
    }

    public Food DtoToFood(FoodDto foodDto){
        return this.modelMapper.map(foodDto, Food.class);
    }

    public FoodDto foodToDto(Food food){
        return this.modelMapper.map(food, FoodDto.class);
    }

    public FoodDto getFood(Long foodId){
        Food food = this.foodRepo.findById(foodId).orElseThrow(()-> new ResourceNotFoundException("Movie", "movieId", foodId));
        return this.foodToDto(food);
    }

    public FoodDto uploadImage(MultipartFile image, Long foodId) throws IOException {
        FoodDto foodDto = this.getFood(foodId);
        String fileName = fileService.uploadImage(path, image);
        foodDto.setImage(fileName);
        return updateFoodDto(foodDto, foodId);
    }
}
