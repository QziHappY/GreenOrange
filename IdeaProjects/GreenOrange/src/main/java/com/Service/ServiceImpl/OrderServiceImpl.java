package com.Service.ServiceImpl;

import com.Controller.ViewObject.OrderVo;
import com.Dao.DataObject.ItemAttributesDO;
import com.Dao.DataObject.OrderAttributesDO;
import com.Dao.DataObject.OrderDO;
import com.Dao.ItemAttributesDOMapper;
import com.Dao.OrderAttributesDOMapper;
import com.Dao.OrderDOMapper;
import com.Service.IService.OrderIService;
import com.Service.Model.OrderAttributesModel;
import com.Service.Model.OrderModel;
import com.Utils.OrderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderIService {
    @Autowired
    private OrderDOMapper orderDOMapper;
    @Autowired
    private OrderAttributesDOMapper orderAttributesDOMapper;
    @Autowired
    private ItemAttributesDOMapper itemAttributesDOMapper;
    @Override
    @Transactional
    public String createOrder(int userid, ArrayList<OrderAttributesDO> list, int addressid) throws ParseException {
        OrderDO orderModel=new OrderDO();
        orderModel.setOrderid(UUID.randomUUID().toString().trim().replaceAll("-", ""));//生成uuid
        orderModel.setUserid(userid);
        orderModel.setState("待支付");
        String serialNumber= OrderUtils.getSerialNumber(list.size());
        orderModel.setOrdernumber(serialNumber);//订单流水号
        orderModel.setCreatedate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//创建日期
        BigDecimal allprice=new BigDecimal(0);//原价
        BigDecimal finalprice=new BigDecimal(0);//最终价格
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
            //减少库存
            itemAttributesDOMapper.reduceShock(itemAttributesDO.getItemattributesid(),list.get(i).getAmount());
            int result=orderAttributesDOMapper.insert(orderAttributesDO);
            if(result<=0){
                return "-1";
            }
        }
        orderModel.setAllprice(allprice);//原价
        orderModel.setFinalallprice(finalprice);//最终价格
        String address="临时地址";//getAddressByID(addressid);//根据id获取地址信息
        orderModel.setArrivaladdress(address);//地址
        orderDOMapper.insert(orderModel);
        return orderModel.getOrderid();
    }

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
