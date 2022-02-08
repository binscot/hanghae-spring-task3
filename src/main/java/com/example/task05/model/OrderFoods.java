package com.example.task05.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter// get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
public class OrderFoods {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Food food;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int price;

    @ManyToOne
    private Orders orders;



//    public OrderFoods(OrderFoodsRequestDto orderFoodsRequestDto){
//        this.foodName = orderFoodsRequestDto.getName();
//        this.quantity = orderFoodsRequestDto.getQuantity();
//        this.price = orderFoodsRequestDto.getPrice();
//    }
//
//    @ManyToOne
//    private Order order;


    //    public OrderFoods(int price, int quantity){
//        this.foodName=food.getName();
//        this.quantity=quantity;
//        this.price = price;
//
//    }
//
    public OrderFoods(Food food, String name, int quantity, int price) {
        this.food = food;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
}
