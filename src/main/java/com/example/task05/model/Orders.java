package com.example.task05.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter// get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
public class Orders {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String restaurantName;

    @OneToMany
    @JoinColumn(name = "ORDERS_ID")
    private List<OrderFoods> orderFoods;

    @Column(nullable = false)
    private int totalPrice;

    @Column
    private int deliveryFee;


    public Orders(String restaurantName, List<OrderFoods> orderFoods, int totalPrice,int deliveryFee) {
        this.restaurantName = restaurantName;
        this.totalPrice = totalPrice;
        this.orderFoods=orderFoods;
        this.deliveryFee=deliveryFee;
    }


}
