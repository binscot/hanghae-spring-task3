package com.example.task05.repository;

import com.example.task05.model.OrderFoods;
import com.example.task05.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderFoodsRepository extends JpaRepository<OrderFoods,Long> {
    List<OrderFoods> findOlderFoodsByOrders(Orders orders);
}


