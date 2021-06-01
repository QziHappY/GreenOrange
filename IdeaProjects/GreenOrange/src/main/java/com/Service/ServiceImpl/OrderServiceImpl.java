package com.Service.ServiceImpl;

import com.Controller.ReturnType.CommonReturnType;
import com.Controller.ViewObject.OrderVo;
import com.Dao.DataObject.ItemAttributesDO;
import com.Dao.DataObject.OrderAttributesDO;
import com.Dao.DataObject.OrderDO;
import com.Dao.ItemAttributesDOMapper;
import com.Dao.OrderAttributesDOMapper;
import com.Dao.OrderDOMapper;
import com.Service.IService.ItemISerive;
import com.Service.IService.OrderIService;
import com.Service.Model.ItemModel;
import com.Service.Model.OrderAttributesModel;
import com.Utils.OrderUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl implements OrderIService {
    @Autowired
    private OrderDOMapper orderDOMapper;
    @Autowired
    private OrderAttributesDOMapper orderAttributesDOMapper;
    @Autowired
    private ItemAttributesDOMapper itemAttributesDOMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    ItemISerive itemServiceImpl;

    //提交订单：submit提交、rollback回滚
    @Override
    @Transactional
    public CommonReturnType submitOrder(ArrayList<OrderAttributesModel> list, String type) throws InterruptedException {
        //效验
        for(int i=0;i<list.size();i++){
            //判断是否售完
            if(redisTemplate.opsForValue().get("soldOut_"+list.get(i).getItemattarbutesid())!=null){
                return CommonReturnType.create("error","产品已售完");
            }
            //setnx：setIfAbsent（存在返回0，不存在创建返回1）
            while(!redisTemplate.opsForValue().setIfAbsent("lock_"+list.get(i).getItemid(),"true")){
                Thread.sleep(1000);
            }
            try{
                //加锁
                redisTemplate.opsForValue().set("lock_"+list.get(i).getItemid(),"true");//商品加锁需要过期时间吗？
                //获取商品信息
                ItemModel itemModel=(ItemModel)redisTemplate.opsForValue().get("item_"+list.get(i).getItemid());
                //redis缓存未命中
                if(itemModel==null){
                    itemModel=itemServiceImpl.getItemById(list.get(i).getItemid());
                    redisTemplate.opsForValue().set("item_"+list.get(i).getItemid(),itemModel,1, TimeUnit.HOURS);
                }
                //数据库未命中
                if(itemModel==null){
                    return CommonReturnType.create("error","产品不存在，提交了错误订单");
                }
                //对应产品的属性效验
                int flag=0;
                for(int j=0;j<itemModel.getItemAttributesList().size();j++){
                    if(StringUtils.equals(itemModel.getItemAttributesList().get(j).getItemattributesid().toString(),list.get(i).getItemattarbutesid().toString())){
                        flag=1;//选择了存在的商品属性
                        //提交订单、查看库存状态、减库存
                        int stock=itemModel.getItemAttributesList().get(j).getStock();
                        if(StringUtils.equals(type,"submit")){
                            //是否已经售完
                            if(stock<=0){
                                redisTemplate.opsForValue().set("soldOut_"+list.get(i).getItemattarbutesid(),"true",1,TimeUnit.HOURS);
                                return CommonReturnType.create("error","产品已售完");
                            }
                            //购买数量超标
                            if(list.get(i).getAmount()>stock){
                                return CommonReturnType.create("error","购买数量超标");
                            }
                            //销量增加
                            itemModel.setSales(itemModel.getSales()+list.get(i).getAmount());
                            //设置减少库存
                            itemModel.getItemAttributesList().get(j).setStock(stock-list.get(i).getAmount());
                            //异步到数据库减少库存
                            itemServiceImpl.updateStock(itemModel.getItemAttributesList().get(j).getItemattributesid(),list.get(i).getAmount(),"reduce");
                        }else{
                            //设置回滚库存
                            itemModel.getItemAttributesList().get(j).setStock(stock+list.get(i).getAmount());
                            //异步到数据库减少库存
                            itemServiceImpl.updateStock(itemModel.getItemAttributesList().get(j).getItemattributesid(),list.get(i).getAmount(),"add");
                            //如果库存大于0，删除售完标识
                            if(itemModel.getItemAttributesList().get(j).getStock()>0){
                                redisTemplate.delete("soldOut_"+list.get(i).getItemattarbutesid());
                            }
                        }
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
                //更新缓存库存
                redisTemplate.opsForValue().set("item_"+itemModel.getItemid(),itemModel);
            }catch (Exception e){

            }finally{
                //释放锁
                redisTemplate.delete("lock_"+list.get(i).getItemid());
            }
        }
        return CommonReturnType.create("");
    }

    //生成订单信息并提交订单
    @Override
    @Transactional
    public CommonReturnType createOrder(int userid, ArrayList<OrderAttributesModel> list, int addressid) throws InterruptedException {
        CommonReturnType tips=submitOrder(list,"submit");//提交订单、减库存
        if(tips.getStatus()!="success"){
            return tips;
        }
        //生成订单信息
        OrderDO orderModel=new OrderDO();
        orderModel.setOrderid(UUID.randomUUID().toString().trim().replaceAll("-", ""));//生成uuid
        orderModel.setUserid(userid);
        orderModel.setState("待支付");
        String serialNumber= OrderUtils.getSerialNumber(list.size());
        orderModel.setOrdernumber(serialNumber);//订单流水号
        orderModel.setCreatedate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//创建日期
        BigDecimal allprice=new BigDecimal(0);//原价
        BigDecimal finalprice=new BigDecimal(0);//最终价格
        //订单商品属性
        for(int i=0;i<list.size();i++){
            //获取当前该商品的该属性
            ItemAttributesDO itemAttributesDO=itemAttributesDOMapper.selectByPrimaryKey(list.get(i).getItemattarbutesid());
            //生成订单的单件商品属性
            OrderAttributesDO orderAttributesDO=new OrderAttributesDO();
            orderAttributesDO.setAmount(list.get(i).getAmount());
            orderAttributesDO.setItemid(itemAttributesDO.getItemid());
            orderAttributesDO.setOrderid(orderModel.getOrderid());
            orderAttributesDO.setPrice(itemAttributesDO.getPrice());
            orderAttributesDO.setItemattarbutesid(itemAttributesDO.getItemattributesid());
            //成交价格计算
            if(list.get(i).getActivityid()!=null){
                //活动价格
            }else{
                //原价
                orderAttributesDO.setFinalprice(itemAttributesDO.getPrice());
            }
            //合计原先价格
            allprice=allprice.add(orderAttributesDO.getPrice().multiply(new BigDecimal(list.get(i).getAmount())));
            //合计最终价格
            finalprice=finalprice.add(orderAttributesDO.getFinalprice().multiply(new BigDecimal(list.get(i).getAmount())));
            int result=orderAttributesDOMapper.insert(orderAttributesDO);
            if(result<=0){
                return CommonReturnType.create("error","生成订单属性信息时发生异常错误");
            }
        }
        orderModel.setAllprice(allprice);//原价
        orderModel.setFinalallprice(finalprice);//最终价格
        String address="临时地址";//getAddressByID(addressid);//根据id获取地址信息
        orderModel.setArrivaladdress(address);//地址
        //提交订单到数据库
        orderDOMapper.insert(orderModel);
        //rabbitmq延时队列：超时自动取消订单
        rabbitTemplate.convertAndSend("createOrderDirectExchange","createOrderDirectExchange",orderModel.getOrderid(),new MessagePostProcessor() {
            //处理发送消息
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置有效期30分钟
                message.getMessageProperties().setExpiration("1800000");
                //message.getMessageProperties().setExpiration("1000");//1000是1s
                return message;
            }});
        return CommonReturnType.create(orderModel.getOrderid());
    }

    //修改订单状态
    @Override
    public int updateState(String orderid,String state) {
        int result=orderDOMapper.updateStateByOrderId(orderid,state);
        orderAttributesDOMapper.updateStateByOrderId(orderid,state);
        return result;
    }

    @Override
    public ArrayList<OrderVo> getOrderListByUserid(int userid) {
        ArrayList<OrderDO> orderDOS=orderDOMapper.selectByUserid(userid);
        ArrayList<OrderVo> orderVos=new ArrayList<>();
        for(int i=0;i<orderDOS.size();i++){
            OrderVo orderVo=new OrderVo(orderDOS.get(i));
            //orderVo.setAttributesModels();
            orderVos.add(orderVo);
        }
        return orderVos;
    }

    @Override
    public OrderVo getOrderByid(String orderid) {
        OrderDO orderDO=orderDOMapper.selectByPrimaryKey(orderid);
        if(orderDO==null){
            return null;
        }
        OrderVo orderVo=new OrderVo(orderDO);
        return orderVo;
    }

    @Override
    public int deleteOrder(String orderid) {
        int result=orderDOMapper.deleteByPrimaryKey(orderid);
        orderAttributesDOMapper.deleteByOrderid(orderid);
        return result;
    }
}
