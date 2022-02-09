package com.example.task05.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RestaurantRequestDto {
    private String name;
    private int minOrderPrice;
    private int deliveryFee;
    private int x;
    private int y;
}