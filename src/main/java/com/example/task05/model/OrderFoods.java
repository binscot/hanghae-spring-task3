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

    @ManyToOne
    private Food food;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    private Orders orders;


    public OrderFoods(Food food, int quantity) {
        this.food = food;
        this.quantity = quantity;
    }
}