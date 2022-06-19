package com.example.orderservice.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.example.orderservice.config.MessageConfig.QUEUE_INVENTORY;
import static com.example.orderservice.config.MessageConfig.QUEUE_ORDER;
@Log4j2
@Component
public class ReceiveMessage {
//    @RabbitListener(queues = {QUEUE_INVENTORY})
//    public void getMessage(String a) {
//        System.out.println(a);
//    }
}
