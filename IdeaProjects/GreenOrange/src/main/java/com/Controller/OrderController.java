package com.Controller;

import com.Controller.ReturnType.CommonReturnType;
import com.Controller.ViewObject.OrderVo;
import com.Dao.DataObject.ItemAttributesDO;
import com.Dao.DataObject.OrderAttributesDO;
import com.Service.IService.ItemISerive;
import com.Service.IService.OrderIService;
import com.Service.Model.ItemModel;
import com.Service.Model.OrderAttributesModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.util.Strings;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Controller("order")
@RequestMapping("/order")
@CrossOrigin
public class OrderController {

    @Autowired
    OrderIService orderServiceImpl;
    @Autowired
    ItemISerive itemServiceImpl;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 提交订单
     */
    @RequestMapping("/createorder")
    @ResponseBody
    public CommonReturnType createorder(@RequestBody ArrayList<OrderAttributesDO> list) throws ParseException {
        String token=httpServletRequest.getHeader("token");
        if(StringUtils.isEmpty(token)||redisTemplate.opsForValue().get(token+"_userid")==null){
            return CommonReturnType.create("error","未登录");
        }
        //用户效验
        String userid=redisTemplate.opsForValue().get(token+"_userid").toString();
        if(StringUtils.isEmpty(userid)||StringUtils.equals(userid,"")){
            return CommonReturnType.create("error","登录失效");
        }
        for(int i=0;i<list.size();i++){
            //redis加锁
            //redisTemplate.opsForValue()
            ItemModel itemModel=(ItemModel)redisTemplate.opsForValue().get("item_"+list.get(i).getItemid());
            //缓存未命中
            if(itemModel==null){
                itemModel=itemServiceImpl.getItemById(list.get(i).getItemid());
                redisTemplate.opsForValue().set("item_"+list.get(i).getItemid(),itemModel,1, TimeUnit.HOURS);
            }
            //数据库未命中
            if(itemModel==null){
                return CommonReturnType.create("error","产品不存在");
            }
            //对应产品的属性效验
            int after=-1,before=-1;
            int flag=0;
            for(int j=0;j<itemModel.getItemAttributesList().size();j++){
                if(StringUtils.equals(itemModel.getItemAttributesList().get(j).getItemattributesid().toString()
                        ,list.get(i).getItemattarbutesid().toString())){
                    flag=1;
                    //库存效验
                    int stock=itemModel.getItemAttributesList().get(j).getStock();
                    if(stock<=0){
                        return CommonReturnType.create("error","产品已售完");
                    }
                    //减库存
                    before=itemModel.getItemAttributesList().get(j).getStock();
                    itemModel.getItemAttributesList().get(j).setStock(stock-list.get(i).getAmount());
                    after=itemModel.getItemAttributesList().get(j).getStock();
                    break;
                }
            }
            if(flag==0){
                return CommonReturnType.create("error","产品不存在该属性");
            }
            //产品参与的活动效验
            if(itemModel.getActivityid()!=null) {
                //活动效验
            }
            redisTemplate.opsForValue().set("item_"+itemModel.getItemid(),itemModel);
            //异步修改数据库的库存
            //System.out.println("Userid="+userid+" ：下单产品id= "+itemModel.getItemid()+" 下单后库存为："+t+);
        }
        int addressid=0;//地址id
        String orderid=orderServiceImpl.createOrder(Integer.parseInt(userid),list,addressid);
        //rabbitmq延时队列：超时自动取消订单
        rabbitTemplate.convertAndSend("createOrderDirectExchange","createOrderDirectExchange",orderid,new MessagePostProcessor() {
                    //处理发送消息
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        //设置有效期30分钟
                        message.getMessageProperties().setExpiration("1800000");
                        //message.getMessageProperties().setExpiration("1000");//1000是1s
                        return message;
                    }});
        return CommonReturnType.create(orderid);
    }
    /**
     * 支付订单
     */
    @RequestMapping("/pay")
    @ResponseBody
    public CommonReturnType pay(@RequestParam("orderid")String orderid){
        String token=httpServletRequest.getHeader("token");
        if(StringUtils.isEmpty(token)){
            return CommonReturnType.create("error","未登录");
        }
        //用户效验
        String userid=redisTemplate.opsForValue().get(token+"_userid").toString();
        if(StringUtils.isEmpty(userid)||StringUtils.equals(userid,"")){
            return CommonReturnType.create("error","登录失效");
        }
        int result=orderServiceImpl.updateState(orderid,"已支付");
        if(result>0){
            return CommonReturnType.create("");
        }else{
            return CommonReturnType.create("error","支付失败");
        }
    }

    /**
     * 获取用户订单列表
     */
    @RequestMapping("/getOrderList")
    @ResponseBody
    public CommonReturnType getOrderList(){
        String token=httpServletRequest.getHeader("token");
        if(StringUtils.isEmpty(token)){
            return CommonReturnType.create("error","未登录");
        }
        //用户效验
        String id=redisTemplate.opsForValue().get(token+"_userid").toString();
        if(StringUtils.isEmpty(id)||StringUtils.equals("",id)){
            return CommonReturnType.create("error","登录失效");
        }
        int userid=Integer.parseInt(id);
        ArrayList<OrderVo> orderVos=orderServiceImpl.getOrderListByUserid(userid);
        return CommonReturnType.create(orderVos);
    }
    /**
     * 获取订单详情
     */
    @RequestMapping("/getOrder")
    @ResponseBody
    public CommonReturnType getOrder(@RequestParam("orderid")String orderid){
        String token=httpServletRequest.getHeader("token");
        if(StringUtils.isEmpty(token)){
            return CommonReturnType.create("error","未登录");
        }
        //用户效验
        String userid=redisTemplate.opsForValue().get(token+"_userid").toString();
        if(StringUtils.isEmpty(userid)||StringUtils.equals(userid,"")){
            return CommonReturnType.create("error","登录失效");
        }
        OrderVo orderVo=orderServiceImpl.getOrderByid(orderid);
        return CommonReturnType.create(orderVo);
    }
    /**
     * 取消订单
     */
    @RequestMapping("/cancelOrder")
    @ResponseBody
    public CommonReturnType cancelOrder(@RequestParam("orderid")String orderid){
        String token=httpServletRequest.getHeader("token");
        if(StringUtils.isEmpty(token)){
            return CommonReturnType.create("error","未登录");
        }
        //用户效验
        String userid=redisTemplate.opsForValue().get(token+"_userid").toString();
        if(StringUtils.isEmpty(userid)||StringUtils.equals(userid,"")){
            return CommonReturnType.create("error","登录失效");
        }
        int result=orderServiceImpl.updateState(orderid,"已取消");
        if(result>0){
            return CommonReturnType.create("");
        }else{
            return CommonReturnType.create("error","修改为已取消状态失败");
        }
    }
    /**
     * 删除订单
     */
    @RequestMapping("/deleteOrder")
    @ResponseBody
    public CommonReturnType deleteOrder(@RequestParam("orderid")String orderid){
        String token=httpServletRequest.getHeader("token");
        if(StringUtils.isEmpty(token)){
            return CommonReturnType.create("error","未登录");
        }
        //用户效验
        String userid=redisTemplate.opsForValue().get(token+"_userid").toString();
        if(StringUtils.isEmpty(userid)||StringUtils.equals(userid,"")){
            return CommonReturnType.create("error","登录失效");
        }
        int result=orderServiceImpl.deleteOrder(orderid);
        if(result>0){
            return CommonReturnType.create("");
        }else{
            return CommonReturnType.create("error","订单删除失败");
        }
    }
    /**
     * 订单发货
     */
    @RequestMapping("/deliverOrder")
    @ResponseBody
    public CommonReturnType deliverOrder(@RequestParam("orderid")String orderid){
        int result=orderServiceImpl.updateState(orderid,"已发货");
        if(result>0){
            return CommonReturnType.create("");
        }else{
            return CommonReturnType.create("error","修改为发货状态失败");
        }
    }
    /**
     * 确认收货
     */
    @RequestMapping("/confirmOrder")
    @ResponseBody
    public CommonReturnType confirmOrder(@RequestParam("orderid")String orderid){
        String token=httpServletRequest.getHeader("token");
        if(StringUtils.isEmpty(token)){
            return CommonReturnType.create("error","未登录");
        }
        //用户效验
        String userid=redisTemplate.opsForValue().get(token+"_userid").toString();
        if(StringUtils.isEmpty(userid)||StringUtils.equals(userid,"")){
            return CommonReturnType.create("error","登录失效");
        }
        int result=orderServiceImpl.updateState(orderid,"已完成");
        if(result>0){
            return CommonReturnType.create("");
        }else{
            return CommonReturnType.create("error","修改为已收货状态失败");
        }
    }
    /**
     * 提交订单评价
     */

    /**
     * 申请退换
     */
    @RequestMapping("/specifications")
    @ResponseBody
    public CommonReturnType specifications(@RequestParam("orderid")String orderid){
        String token=httpServletRequest.getHeader("token");
        if(StringUtils.isEmpty(token)){
            return CommonReturnType.create("error","未登录");
        }
        //用户效验
        String userid=redisTemplate.opsForValue().get(token+"_userid").toString();
        if(StringUtils.isEmpty(userid)||StringUtils.equals(userid,"")){
            return CommonReturnType.create("error","登录失效");
        }
        int result=orderServiceImpl.updateState(orderid,"退换中");
        if(result>0){
            return CommonReturnType.create("");
        }else{
            return CommonReturnType.create("error","修改为退换中状态失败");
        }
    }
    /**
     * 提醒发货
     */
}
