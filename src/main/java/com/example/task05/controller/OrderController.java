package com.example.task05.controller;

import com.example.task05.dto.OrderRequestDto;
import com.example.task05.dto.OrdersResponseDto;
import com.example.task05.model.Orders;
import com.example.task05.repository.OrderRepository;
import com.example.task05.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;


    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }


    @PostMapping("/order/request")
    public OrdersResponseDto createOrder(@RequestBody OrderRequestDto orderRequestDto){

        return orderService.createOrder(orderRequestDto);
    }


}


