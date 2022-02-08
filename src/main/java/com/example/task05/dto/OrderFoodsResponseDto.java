package com.example.task05.dto;

import com.example.task05.model.OrderFoods;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderFoodsResponseDto {
    private String name;
    private int quantity;
    private int price;

    public OrderFoodsResponseDto(OrderFoods orderFoods) {
        this.name=orderFoods.getName();
        this.quantity=orderFoods.getQuantity();
        this.price=orderFoods.getPrice();
    }
}
