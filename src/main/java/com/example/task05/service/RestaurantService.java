package com.example.task05.service;

import com.example.task05.dto.LocationDto;
import com.example.task05.dto.RestaurantRequestDto;
import com.example.task05.model.Restaurant;
import com.example.task05.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
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

    @Transactional
    public List<Restaurant> showRestaurant(LocationDto locationDto) {
        //보여줄 레스토랑 리스트
        List<Restaurant> showRestaurants = new ArrayList<>();
        //거리를 비교해줄 레스토랑 리스트
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        for (Restaurant restaurant : restaurantList){
            int x = restaurant.getX();
            System.out.println("레스토랑 x:"+x);
            int y = restaurant.getY();
            System.out.println("레스토랑 y:"+y);
            int userX = locationDto.getX();
            System.out.println("user x:"+userX);
            int userY = locationDto.getY();
            System.out.println("user y:"+userY);
            int xx = x-userX;
            int yy = y-userY;
            if (xx<0){
                xx *= -1;
            }
            System.out.println(xx);
            if (yy<0){
                yy *= -1;
            }
            System.out.println(yy);
            int z = xx+yy;
            System.out.println(z);
            if (z<4){
                showRestaurants.add(restaurant);
            }
            System.out.println(showRestaurants);
        }
        return showRestaurants;

    }
}
