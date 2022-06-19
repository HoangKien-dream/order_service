package com.example.orderservice.service;

import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderDetail;
import com.example.orderservice.entity.OrderDetailKey;
import com.example.orderservice.entity.Product;
import com.example.orderservice.enums.Status;
import com.example.orderservice.repository.RepositoryOrder;
import com.example.orderservice.repository.RepositoryProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class OrderService {
    @Autowired
    RepositoryOrder repositoryOrder;
    @Autowired
    RepositoryProduct repositoryProduct;

    public Order save(Order order){
        int totalPrice = 0;
        Set<OrderDetail> orderDetailSet = new HashSet<>();
        for (OrderDetail orderDetail:
                order.getOrderDetails()) {
            Product product = repositoryProduct.findById(orderDetail.getProductId()).orElse(null);
            if (product != null){
                orderDetail.setId(new OrderDetailKey(order.getId(), product.getId()));
                orderDetail.setProduct(product);
                orderDetail.setOrder(order);
                orderDetail.setUnitPrice(product.getPrice());
                orderDetailSet.add(orderDetail);
                totalPrice += orderDetail.getUnitPrice() * orderDetail.getQuantity();
            }
        }
        order.setOrderStatus(Status.OrderStatus.PENDING.name());
        order.setInventoryStatus(Status.InventoryStatus.PENDING.name());
        order.setPaymentStatus(Status.PaymentStatus.PENDING.name());
        order.setTotalPrice(totalPrice);
        order.setOrderDetails(orderDetailSet);
        return repositoryOrder.save(order);
    }
}
