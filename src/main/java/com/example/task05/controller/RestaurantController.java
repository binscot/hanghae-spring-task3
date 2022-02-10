package com.example.task05.controller;

import com.example.task05.dto.LocationDto;
import com.example.task05.dto.RestaurantRequestDto;
import com.example.task05.model.Restaurant;
import com.example.task05.service.RestaurantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping("/restaurant/register")
    public Restaurant createRestaurant(@RequestBody RestaurantRequestDto requestDto){
        return restaurantService.createRestaurant(requestDto);
    }

    @GetMapping("/restaurants")
    public List<Restaurant> getRestaurant(LocationDto locationDto) {
        return restaurantService.showRestaurant(locationDto);
    }
}