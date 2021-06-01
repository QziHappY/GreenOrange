package com.rabbitmq;

import com.Controller.ViewObject.OrderVo;
import com.Dao.DataObject.ItemAttributesDO;
import com.Dao.DataObject.OrderDO;
import com.Service.IService.ItemISerive;
import com.Service.IService.OrderIService;
import com.Service.Model.ItemModel;
import com.Service.Model.OrderModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
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
    @Autowired
    ItemISerive itemServiceImpl;
    //处理【订单超时】
    @RabbitListener(queues = "outTimeOrderDlxQueue")
    public void handleOutTimeOrder(String orderid) throws InterruptedException {
        //先查原来的订单：
        OrderVo orderDO=orderServiceImpl.getOrderByid(orderid);
        //未支付修改
        if(orderDO.getState().equals("未支付")){
            orderServiceImpl.updateState(orderid,"已取消");
            //回滚库存
            orderServiceImpl.submitOrder(orderDO.getAttributesModels(),"rollback");
        }
    }
    //异步处理【更新库存】
    @RabbitListener(queues = "updateStockQueue")
    public void handleUpdateStock(int itemAttributesid,int amount,String type){
        itemServiceImpl.updateStock(itemAttributesid,amount,type);
    }
}
