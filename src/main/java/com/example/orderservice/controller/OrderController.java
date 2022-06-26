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
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@Log4j2
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
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String startDate,
            @RequestParam(defaultValue = "") String endDate,
            @RequestParam(defaultValue = "") String status) {
        Specification<Order> specification = Specification.where(null);
        if (keyword != null && keyword.length() > 0) {
            SearchCriteria searchCriteria
                    = new SearchCriteria("name", SearchCriteriaOperator.JOIN, keyword);
            OrderSpecification filter = new OrderSpecification(searchCriteria);
            specification = specification.and(filter);
        }
        if (status != null && status.length() > 0) {
            SearchCriteria searchCriteria
                    = new SearchCriteria("orderStatus", SearchCriteriaOperator.EQUALS, status);
            OrderSpecification filter = new OrderSpecification(searchCriteria);
            specification = specification.and(filter);
        }
//        if (userName != null) {
//            SearchCriteria searchCriteria
//                    = new SearchCriteria("lastName", SearchCriteriaOperator.JOIN_USER, userName);
//            OrderSpecification filter = new OrderSpecification(searchCriteria);
//            specification = specification.and(filter);
//        }
//        if (userPhone != null){
//            SearchCriteria searchCriteria
//                    = new SearchCriteria("phone", SearchCriteriaOperator.JOIN_USER, userPhone);
//            OrderSpecification filter = new OrderSpecification(searchCriteria);
//            specification = specification.and(filter);
//        }
        log.info("check start date: " + startDate);
        if (startDate != null & startDate.length()>1){
            SearchCriteria searchCriteria
                    = new SearchCriteria("createdAt", SearchCriteriaOperator.GREATER_THAN_OR_EQUALS,DateTimeHelper.convertStringToLocalDate(startDate));
            OrderSpecification filter = new OrderSpecification(searchCriteria);
            specification = specification.and(filter);
        }
        if (endDate != null & endDate.length()>1){
            SearchCriteria searchCriteria
                    = new SearchCriteria("createdAt", SearchCriteriaOperator.LESS_THAN_OR_EQUALS, DateTimeHelper.convertStringToLocalDate(endDate));
            OrderSpecification filter = new OrderSpecification(searchCriteria);
            specification = specification.and(filter);
        }
        Page<Order> result = this.orderService.findAll(page, size, specification);
        return ResponseEntity.ok().body(result);
    }
    @RequestMapping(path = "{id}",method = RequestMethod.GET)
    public Order findById(@PathVariable int id){
        return orderService.findById(id);
    }
}
