package com.example.task05.service;

import com.example.task05.dto.LocationDto;
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


    public OrderService(
            RestaurantRepository restaurantRepository,
            FoodRepository foodRepository,
            OrderFoodsRepository orderFoodsRepository,
            OrderRepository orderRepository
    ) {
        this.restaurantRepository = restaurantRepository;
        this.foodRepository = foodRepository;
        this.orderFoodsRepository = orderFoodsRepository;
        this.orderRepository = orderRepository;
    }

    //주문
    //받는정보 : 음식점id, 음식id,음식주문수량
    //보여줄 정보 :
              // 음식점 이름
              // 주문 음식이름,
              // 음식수량, 주문음식가격,
              // 배달비,
              // 최종 결제금액
   @Transactional
    public OrdersResponseDto createOrder(OrderRequestDto orderRequestDtoList) {
        //음식점 이름
        Restaurant restaurant = restaurantRepository.findById(orderRequestDtoList.getRestaurantId())
                .orElseThrow(
                        () -> new NullPointerException("해당 음식점이 없습니다.")
                );
        // 고객에게 보여줄 주문음식이름,음식수량,음식가격을 담아줄 리스트
        List<OrderFoodsResponseDto> orderFoodsResponseDtoList = new ArrayList<>();
        // 테이블에 저장할 주문음식,음식수량,주문정보를 담아줄 리스트
        List<OrderFoods> orderFoodsList =new ArrayList<>();
        // 배달비까지 포함한 총 금액을 담아줄 변수
        int totalPrice =0;
        // 주문정보에서 음식정보들을 하나씩 빼내서리스트에 담아줌
        for (OrderFoods orderRequestDto: orderRequestDtoList.getFoods()){
            //음식 수량을빼냄
            int quantity = orderRequestDto.getQuantity();
            //음식 수량 허용 조건
            if (quantity < 0 || 100 < quantity){
                throw new IllegalArgumentException("음식은 1개이상 100개 이하로 주문 할 수 있습니다.");
            }
            // 주문정보에서 음식id를 받아 food로 음식정보를 찾음
            Food food = foodRepository.findById(orderRequestDto.getId())
                    .orElseThrow(() -> new NullPointerException("해당 음식이 없습니다."));
            // 주문음식 테이블에 음식id로찾음 음식정보와 주문음식 갯수를 받아 저장
            OrderFoods orderFoods = new OrderFoods(food,quantity);
            orderFoodsRepository.save(orderFoods);
            // 주문 음식 리스트에 음식정보와 주문음식 갯수를 넣어줌
            orderFoodsList.add(orderFoods);

            // 총 음식가격을 계산
            totalPrice+=food.getPrice()*quantity;

            // 음식정보를 보여줄 리스트를 만들어 저장해준 리스트에서 음식이름,수량,가격을 리스트에 넣어줌
            OrderFoodsResponseDto orderFoodsResponseDto = new OrderFoodsResponseDto(
                    orderFoods.getFood().getName(),
                    quantity,
                    orderFoods.getFood().getPrice()*quantity);

            // 하나씩넣어준 리스트를 주문한 모든음식정보가 있는 리스트에 넣어줌
            orderFoodsResponseDtoList.add(orderFoodsResponseDto);
        }
        // 총 주문금액이 음식점 최소주문 가격보다 작은지 확인
        if (totalPrice<restaurant.getMinOrderPrice()){
            throw new IllegalArgumentException("최소주문금액 이상 주문해주세요!");
        }
        // 음식점의 배달비를 뺴냄
        int deliveryFee = restaurant.getDeliveryFee();
        deliveryFee += deliveryFeePolicy(orderRequestDtoList.getLocationDto(),restaurant);
        // 총가격에 배달비를 더해줌
        totalPrice+=deliveryFee;
        // 주문을 저장해줌
        Orders orders = new Orders(
                restaurant.getName(),
                orderFoodsList,totalPrice,
                restaurant.getDeliveryFee()
        );
        orderRepository.save(orders);

        // 고객에게 주문정보를 보여줌
        return new OrdersResponseDto(
                orders,
                orderFoodsResponseDtoList,
                deliveryFee
        );

    }

    @Transactional
    public  List<OrdersResponseDto> showOrders(){
        // 고객에게 보여줄 주문정보들의 리스트
        List<OrdersResponseDto> orderFoodsResponseDtoList =new ArrayList<>();
        // 보여줄 주문정보중 음식정보를 담을 리스트
        List<OrderFoodsResponseDto> ordersResponse = new ArrayList<>();
        // 주문 테이블을 리스트로 만들어 찾음
        List<Orders> ordersList = orderRepository.findAll();
        // 주문정보에서 정보를 하나씩 뺴냄
        for (Orders orders:ordersList){
            // 배달비를 빼냄
            int deliveryFee = orders.getDeliveryFee();
            // 주문정보중에 음식정보를 빼냄
            List<OrderFoods> orderFoodsList  = orderFoodsRepository.findOlderFoodsByOrders(orders);
            // 빼낸 음식정보리스트에서 음식정보를 하나씩 빼냄
            for (OrderFoods orderFoods:orderFoodsList){
                // 빼낸 음식정보를 보여줄 정보로 바꿈
                OrderFoodsResponseDto orderFoodsResponseDto = new OrderFoodsResponseDto(
                        orderFoods.getFood().getName(),
                        orderFoods.getQuantity(),
                        orderFoods.getFood().getPrice()*orderFoods.getQuantity());
                //보여줄 정보로 바꾼 음식 하나씩 보여줄 리스트에 넣어줌
                ordersResponse.add(orderFoodsResponseDto);
            }
            //주문 정보를 하나씩 만들어줌
            OrdersResponseDto ordersResponseDto =  new OrdersResponseDto(orders,ordersResponse,deliveryFee);
            //하나씩 만든 주문정보들을 주문정보들의 리스트에 넣어줌
            orderFoodsResponseDtoList.add(ordersResponseDto);
        }
        //보여줌
        return orderFoodsResponseDtoList;
    }

    //배달비 할증
    @Transactional
    public int deliveryFeePolicy(LocationDto locationDto, Restaurant restaurant){
        int deliveryFeePolicy = 0;
        int distanceXY = RestaurantService.getDistance(locationDto, restaurant);
        if (distanceXY>3){
            throw new IllegalArgumentException("해당음식점은 거리가 멀어 주문할 수 없습니다!!");
        } else {
            deliveryFeePolicy = distanceXY * 500;
        }
        return deliveryFeePolicy;
    }




}
