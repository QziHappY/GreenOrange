package com.rabbitmq.Config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class queue {
    //异步【提交订单】执行到数据库的队列
    @Bean(name = "createOrderQueue")
    public Queue getCreateOrderQueue(){
        Map<String, Object> arguments = new HashMap<>(2);
        // 绑定【订单超时】死信交换机
        arguments.put("x-dead-letter-exchange", "outTimeOrderDlxExchange");
        // 绑定【订单超时】路由key：
        arguments.put("x-dead-letter-routing-key", "to_outTimeOrderDlxQueue");
        return new Queue("createOrderQueue", true, false, false, arguments);
    }

    //【订单超时】死信队列
    @Bean(name = "outTimeOrderDlxQueue")
    public Queue getOutTimeOrderDlxQueue(){
        return new Queue("outTimeOrderDlxQueue");
    }

    //异步【更新库存】执行到数据库的队列
    @Bean(name = "updateStockQueue")
    public Queue getUpdateStockQueue(){
        return new Queue("updateStockQueue", true, false, false, null);
    }
}
