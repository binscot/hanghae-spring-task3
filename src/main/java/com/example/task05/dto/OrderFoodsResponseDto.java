package com.example.task05.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderFoodsResponseDto {
    private String name;
    private int quantity;
    private int price;


    public OrderFoodsResponseDto(String name, int quantity, int price) {
        this.name=name;
        this.quantity=quantity;
        this.price=price;
    }
}
