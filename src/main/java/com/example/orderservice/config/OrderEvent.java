package com.example.orderservice.config;

import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderDetail;
import lombok.*;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    public int orderId;
    public int userId;
    public int totalPrice;
    public Set<OrderDetailEvent> orderDetailEvents;
    public String statusInventory;
    public String statusOrder;
    public String statusPayment;
    public String queueName;

    public OrderEvent(Order order) {
        this.orderId = order.getId();
        this.userId = order.getUserId();
        this.totalPrice = order.getTotalPrice();
        this.statusPayment = order.getPaymentStatus();
        this.statusInventory = order.getInventoryStatus();
        this.statusOrder = order.getOrderStatus();
        Set<OrderDetailEvent> list = new HashSet<>();
        for (OrderDetail orderDetails:
             order.getOrderDetails()) {
            list.add(new OrderDetailEvent(orderDetails));
        }
        this.orderDetailEvents = list;
    }

}
