package com.example.task05.controller;

import com.example.task05.dto.OrderRequestDto;
import com.example.task05.dto.OrdersResponseDto;
import com.example.task05.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {
    private final OrderService orderService;



    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/order/request")
    public OrdersResponseDto createOrder(@RequestBody OrderRequestDto orderRequestDto){

        return orderService.createOrder(orderRequestDto);
    }

    @GetMapping("/orders")
    public List<OrdersResponseDto> showOrders() {
        return orderService.showOrders();
    }


}


