package com.Service.IService;

import com.Controller.ViewObject.OrderVo;
import com.Dao.DataObject.OrderAttributesDO;
import com.Service.Model.OrderAttributesModel;

import java.text.ParseException;
import java.util.ArrayList;

public interface OrderIService {
    String createOrder(int userid, ArrayList<OrderAttributesDO> list, int addressid) throws ParseException;

    int updateState(String orderid,String state);

    ArrayList<OrderVo> getOrderListByUserid(int userid);

    OrderVo getOrderByid(String orderid);

    int deleteOrder(String orderid);

}
