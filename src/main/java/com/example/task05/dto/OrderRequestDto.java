package com.example.task05.dto;

import com.example.task05.model.OrderFoods;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrderRequestDto {
    public Long restaurantId;
    private List<OrderFoods> foods;
    private LocationDto locationDto;
}
