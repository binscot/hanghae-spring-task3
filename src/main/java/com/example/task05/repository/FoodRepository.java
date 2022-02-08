package com.example.task05.repository;

import com.example.task05.model.Food;
import com.example.task05.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, Long> {

    Optional<Food> findFoodByRestaurantAndName(Restaurant restaurant, String name);
    List<Food> findByRestaurant_Id(Long restaurantId);


}
