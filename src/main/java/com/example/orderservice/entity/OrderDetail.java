package com.example.orderservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    private int productId;
    private int orderId;
    private String name;
    private int quantity;
    private int unitPrice;
}
