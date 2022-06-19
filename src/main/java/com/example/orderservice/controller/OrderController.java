package com.example.orderservice.controller;

import com.example.orderservice.config.MessageConfig;
import com.example.orderservice.config.OrderDetailEvent;
import com.example.orderservice.config.OrderEvent;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.RepositoryOrder;
import com.example.orderservice.service.OrderService;
import com.google.gson.Gson;
import org.aspectj.weaver.ast.Or;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;


@RequestMapping(path = "api/v1/order")
@RestController
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @RequestMapping(method = RequestMethod.POST)
    public Order create(@RequestBody Order order){
       Order order1 = orderService.save(order);
       OrderEvent orderEvent = new OrderEvent(order1);
        Gson gson = new Gson();
        rabbitTemplate.convertAndSend(MessageConfig.DIRECT_EXCHANGE,MessageConfig.DIRECT_SHARE_ROUTING_KEY, gson.toJson(orderEvent));
         return order1;
    }
}
