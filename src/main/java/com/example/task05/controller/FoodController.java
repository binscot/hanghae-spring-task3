package com.example.task05.controller;

import com.example.task05.dto.FoodRequestDto;
import com.example.task05.model.Food;
import com.example.task05.repository.FoodRepository;
import com.example.task05.service.FoodService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FoodController {

    private final FoodService foodService;
    private final FoodRepository foodRepository;


    public FoodController(FoodService foodService, FoodRepository foodRepository) {
        this.foodService = foodService;
        this.foodRepository = foodRepository;
    }

    @PostMapping("/restaurant/{restaurantId}/food/register")
    public void createFood(@PathVariable Long restaurantId,
                           @RequestBody List<FoodRequestDto> foodRequestDto){
        foodService.createFood(restaurantId,foodRequestDto);
    }

    @GetMapping("/restaurant/{restaurantId}/foods")
    public List<Food> getFood(@PathVariable Long restaurantId) {return foodRepository.findByRestaurant_Id(restaurantId);}

}