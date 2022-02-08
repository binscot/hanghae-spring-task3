package com.example.task05.service;

import com.example.task05.dto.RestaurantRequestDto;
import com.example.task05.model.Restaurant;
import com.example.task05.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Restaurant createRestaurant(RestaurantRequestDto requestDto) {
        Restaurant restaurant = new Restaurant(requestDto);
        Optional<Restaurant> found = restaurantRepository.findByName(requestDto.getName());
        if (requestDto.getName() == null){
            throw new NullPointerException("음식점 이름을 입력해주세요!");
        } else if (found.isPresent()){
            throw new IllegalArgumentException("이미 같은 이름의 음식점이 있습니다.");
        } else  if (requestDto.getMinOrderPrice() < 1000 || requestDto.getMinOrderPrice() > 100000){
            throw new IllegalArgumentException("최소주문가격은 1000원이상 100,000원이하 입니다.");
        } else  if (requestDto.getMinOrderPrice() % 100 != 0){
            throw new IllegalArgumentException("최소주문가격은 100원 단위로 입력해주세요!");
        } else if (requestDto.getDeliveryFee() < 0 || requestDto.getDeliveryFee() >10000){
            throw new IllegalArgumentException("배달비는 0원 이상 10,000원 이하로 입력해주세요!");
        } else  if (requestDto.getDeliveryFee()%500 != 0){
            throw new IllegalArgumentException("배달비는 500원 단위로 입력해 주세요!");
        } else {
            return restaurantRepository.save(restaurant);
        }

    }
}
