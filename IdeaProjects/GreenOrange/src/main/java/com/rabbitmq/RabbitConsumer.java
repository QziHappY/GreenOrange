package com.rabbitmq;

import com.Controller.ViewObject.OrderVo;
import com.Dao.DataObject.OrderDO;
import com.Service.IService.OrderIService;
import com.Service.Model.ItemModel;
import com.Service.Model.OrderModel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitConsumer {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    OrderIService orderServiceImpl;
    //处理【订单超时】
    @RabbitListener(queues = "outTimeOrderDlxQueue")
    public void handleOutTimeOrder(String orderid){
        //先查原来的订单：
        OrderVo orderDO=orderServiceImpl.getOrderByid(orderid);
        //未支付修改
        if(orderDO.getState().equals("未支付")){
            orderServiceImpl.updateState(orderid,"已取消");
        }
    }
}
