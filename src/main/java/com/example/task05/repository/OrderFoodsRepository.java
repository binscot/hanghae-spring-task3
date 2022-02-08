package com.example.task05.repository;

import com.example.task05.model.OrderFoods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderFoodsRepository extends JpaRepository<OrderFoods,Long> {
//    List<OrderFoods> findOrderFoodsByOrder(Order order);
}

