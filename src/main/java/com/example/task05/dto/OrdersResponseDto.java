package com.example.task05.dto;

import com.example.task05.model.Orders;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrdersResponseDto {

    private String restaurantName;
    private List<OrderFoodsResponseDto> foods;
    private int deliveryFee;
    private int totalPrice;

    public OrdersResponseDto(Orders orders, List<OrderFoodsResponseDto> orderFoodsResponseDtoList,int deliveryFee) {
        this.restaurantName=orders.getRestaurantName();
        this.foods=orderFoodsResponseDtoList;
        this.deliveryFee=deliveryFee;
        this.totalPrice=orders.getTotalPrice();
    }


}
