package com.Controller;

import com.Controller.ReturnType.CommonReturnType;
import com.Controller.ViewObject.OrderVo;
import com.Service.IService.ItemISerive;
import com.Service.IService.OrderIService;
import com.Service.Model.OrderAttributesModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;

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
    public CommonReturnType createorder(@RequestBody ArrayList<OrderAttributesModel> list) throws ParseException, InterruptedException {
        String token=httpServletRequest.getHeader("token");
        //用户效验
        if(StringUtils.isEmpty(token)||redisTemplate.opsForValue().get(token+"_userid")==null){
            return CommonReturnType.create("error","未登录");
        }
        String userid=redisTemplate.opsForValue().get(token+"_userid").toString();
        if(StringUtils.isEmpty(userid)||StringUtils.equals(userid,"")){
            return CommonReturnType.create("error","登录失效");
        }
        int addressid=0;
        return orderServiceImpl.createOrder(Integer.parseInt(userid),list,addressid);
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
}
