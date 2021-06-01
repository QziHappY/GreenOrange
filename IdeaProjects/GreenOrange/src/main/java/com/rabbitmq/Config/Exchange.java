package com.rabbitmq.Config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Exchange {
    //【提交订单】交换机
    @Bean(name = "createOrderDirectExchange")
    DirectExchange getOrderDirectExchange(){
        return new DirectExchange("createOrderDirectExchange");
    }
    //将【提交订单】交换机与【提交订单】队列绑定：路由"to_createOrderQueue"
    @Bean
    Binding binging_OrderQueue_To_OrderFanoutExchange(@Qualifier("createOrderQueue")Queue createOrderQueue,
                                                      @Qualifier("createOrderDirectExchange") DirectExchange createOrderDirectExchange){
        return BindingBuilder.bind(createOrderQueue).to(createOrderDirectExchange).with("to_createOrderDirectExchange");
    }
    //【订单超时】死信交换机
    @Bean(name = "outTimeOrderDlxExchange")
    DirectExchange getOutTimeOrderDlxExchange(){
        return new DirectExchange("outTimeOrderDlxExchange");
    }
    //将【订单超时】死信交换机与【订单超时】死信队列绑定
    @Bean
    Binding binging_outTimeOrderDlxQueue_To_outTimeOrderDlxExchange(@Qualifier("outTimeOrderDlxQueue")Queue outTimeOrderDlxQueue,
                                                                    @Qualifier("outTimeOrderDlxExchange") DirectExchange outTimeOrderDlxExchange){
        return BindingBuilder.bind(outTimeOrderDlxQueue).to(outTimeOrderDlxExchange).with("to_outTimeOrderDlxQueue");
    }


    //【更新库存】交换机
    @Bean(name = "updateStockDirectExchange")
    DirectExchange getUpdateStockDirectExchange(){
        return new DirectExchange("updateStockDirectExchange");
    }
    //将【更新库存】交换机与【更新库存】队列绑定：路由"to_updateStockQueue"
    @Bean
    Binding binging_updateStockQueue_To_updateStockDirectExchange(@Qualifier("updateStockQueue")Queue updateStockQueue,
                                                      @Qualifier("updateStockDirectExchange") DirectExchange updateStockDirectExchange){
        return BindingBuilder.bind(updateStockQueue).to(updateStockDirectExchange).with("to_updateStockQueue");
    }
}
