package com.example.orderservice.config;

import com.example.orderservice.entity.Order;
import com.example.orderservice.enums.Status;
import com.example.orderservice.repository.RepositoryOrder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
    @Autowired
    RepositoryOrder repositoryOrder;
    @Autowired
    RabbitTemplate rabbitTemplate;
     public void getMessage(OrderEvent orderEvent){
         Order order = repositoryOrder.findById(orderEvent.orderId).orElse(null);
         if(orderEvent.getQueueName().equals("QUEUE_INVENTORY")){
             order.setInventoryStatus(orderEvent.getStatusInventory());
         }
         if(orderEvent.getQueueName().equals("QUEUE_PAY")){
            order.setPaymentStatus(orderEvent.getStatusPayment());
         }
         handleOrder(order);
     }

     public void handleOrder(Order order){
         if(order.getInventoryStatus().equals(Status.InventoryStatus.OUT_OF_STOCK.name())){
             order.setOrderStatus(Status.OrderStatus.CANCEL.name());
             rabbitTemplate.convertAndSend(MessageConfig.DIRECT_EXCHANGE,MessageConfig.DIRECT_ROUTING_KEY_PAY, new OrderEvent(order));
         }
         if (order.getPaymentStatus().equals(Status.PaymentStatus.NOT_ENOUGH_BALANCE.name())){
             order.setOrderStatus(Status.OrderStatus.CANCEL.name());
             rabbitTemplate.convertAndSend(MessageConfig.DIRECT_EXCHANGE,MessageConfig.DIRECT_ROUTING_KEY_INVENTORY, new OrderEvent(order));
         }
         if (order.getPaymentStatus().equals(Status.PaymentStatus.DONE.name())
                 && order.getInventoryStatus().equals(Status.InventoryStatus.DONE.name()) ){
             order.setOrderStatus(Status.OrderStatus.DONE.name());
         }
         repositoryOrder.save(order);
     }
}
