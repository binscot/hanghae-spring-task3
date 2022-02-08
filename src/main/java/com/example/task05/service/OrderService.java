package com.example.task05.service;

import com.example.task05.dto.OrderFoodsResponseDto;
import com.example.task05.dto.OrderRequestDto;
import com.example.task05.dto.OrdersResponseDto;
import com.example.task05.model.Food;
import com.example.task05.model.OrderFoods;
import com.example.task05.model.Orders;
import com.example.task05.model.Restaurant;
import com.example.task05.repository.FoodRepository;
import com.example.task05.repository.OrderFoodsRepository;
import com.example.task05.repository.OrderRepository;
import com.example.task05.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final RestaurantRepository restaurantRepository;
    private final FoodRepository foodRepository;
    private final OrderFoodsRepository orderFoodsRepository;
    private final OrderRepository orderRepository;




    public OrderService(RestaurantRepository restaurantRepository, FoodRepository foodRepository, OrderFoodsRepository orderFoodsRepository, OrderRepository orderRepository) {
        this.restaurantRepository = restaurantRepository;
        this.foodRepository = foodRepository;
        this.orderFoodsRepository = orderFoodsRepository;
        this.orderRepository = orderRepository;

    }

    @Transactional
    public OrdersResponseDto createOrder(OrderRequestDto orderRequestDto) {

        Restaurant restaurant = restaurantRepository.findById(orderRequestDto.getRestaurantId())
                .orElseThrow(
                        () -> new NullPointerException("해당 음식점이 없습니다.")
                );
        List<OrderFoodsResponseDto> orderFoodsResponseDtoList = new ArrayList<>();
        List<OrderFoods> orderFoodsList =new ArrayList<>();
        int totalPrice =0;

        for (OrderFoods tempOrderFood: orderRequestDto.getFoods()){
            int quantity = tempOrderFood.getQuantity();
            if (quantity < 0 || 100 < quantity){
                throw new IllegalArgumentException("음식은 1개이상 100개 이하로 주문 할 수 있습니다.");
            }
            Food food = foodRepository.findById(tempOrderFood.getId())
                    .orElseThrow(() -> new NullPointerException("해당 음식이 없습니다."));


            OrderFoods orderFoods = new OrderFoods(food,quantity);
            orderFoodsRepository.save(orderFoods);

            totalPrice+=food.getPrice()*quantity;
            orderFoodsList.add(orderFoods);

            OrderFoodsResponseDto orderFoodsResponseDto = new OrderFoodsResponseDto(
                    orderFoods.getFood().getName(),
                    quantity,
                    orderFoods.getFood().getPrice()*quantity);

            orderFoodsResponseDtoList.add(orderFoodsResponseDto);

        }
        if (totalPrice<restaurant.getMinOrderPrice()){
            throw new IllegalArgumentException("최소주문금액 이상 주문해주세요!");
        }
        int deliveryFee = restaurant.getDeliveryFee();
        totalPrice+=deliveryFee;
        Orders orders = new Orders(restaurant.getName(),orderFoodsList,totalPrice,restaurant.getDeliveryFee());
        orderRepository.save(orders);
        return new OrdersResponseDto(orders,orderFoodsResponseDtoList,deliveryFee);

    }

    public  List<OrdersResponseDto> showOrders(){
        List<OrderFoodsResponseDto> ordersResponse = new ArrayList<>();
        List<OrdersResponseDto> orderFoodsResponseDtoList =new ArrayList<>();
        List<Orders> ordersList = orderRepository.findAll();
        for (Orders orders:ordersList){
            int deliveryFee = orders.getDeliveryFee();
            String restaurantName = orders.getRestaurantName();
            int totalPrice= orders.getTotalPrice();
            List<OrderFoods> orderFoodsList  = orderFoodsRepository.findOlderFoodsByOrders(orders);
            for (OrderFoods orderFoods:orderFoodsList){
                OrderFoodsResponseDto orderFoodsResponseDto = new OrderFoodsResponseDto(
                        orderFoods.getFood().getName(),
                        orderFoods.getQuantity(),
                        orderFoods.getFood().getPrice()*orderFoods.getQuantity());
                ordersResponse.add(orderFoodsResponseDto);
            }
            OrdersResponseDto ordersResponseDto =  new OrdersResponseDto(orders,ordersResponse,deliveryFee);
            orderFoodsResponseDtoList.add(ordersResponseDto);

        }
        return orderFoodsResponseDtoList;
    }




}
