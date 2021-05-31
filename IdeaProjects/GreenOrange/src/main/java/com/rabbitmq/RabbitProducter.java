package com.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class RabbitProducter {
//     amqpTemplate;
//
//    //异步提交订单执行到数据库的生产者
//    public void orderProducter(int itemid,int amount){
//        HashMap<String,Object> map=new HashMap<>();
//        map.put("itemid",itemid);
//        map.put("amount",amount);
//        amqpTemplate.convertAndSend("orderFanoutExchange","",itemid);
//
//        System.out.println("发送了");
//    }
}
