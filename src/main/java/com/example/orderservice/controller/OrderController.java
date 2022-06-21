package com.example.orderservice.controller;

import com.example.orderservice.config.MessageConfig;
import com.example.orderservice.config.OrderEvent;
import com.example.orderservice.entity.Order;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.specification.OrderSpecification;
import com.example.orderservice.specification.SearchCriteria;
import com.example.orderservice.specification.SearchCriteriaOperator;
import com.example.orderservice.util.DateTimeHelper;
import com.google.gson.Gson;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
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
        rabbitTemplate.convertAndSend(MessageConfig.DIRECT_EXCHANGE,MessageConfig.DIRECT_SHARE_ROUTING_KEY, orderEvent);
         return order1;
    }
}
