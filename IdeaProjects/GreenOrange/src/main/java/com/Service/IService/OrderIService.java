package com.Service.IService;

import com.Controller.ReturnType.CommonReturnType;
import com.Controller.ViewObject.OrderVo;
import com.Dao.DataObject.OrderAttributesDO;
import com.Service.Model.OrderAttributesModel;

import java.text.ParseException;
import java.util.ArrayList;

public interface OrderIService {
    CommonReturnType createOrder(int userid, ArrayList<OrderAttributesModel> list, int addressid) throws ParseException, InterruptedException;

    int updateState(String orderid,String state);

    ArrayList<OrderVo> getOrderListByUserid(int userid);

    OrderVo getOrderByid(String orderid);

    int deleteOrder(String orderid);

    CommonReturnType submitOrder(ArrayList<OrderAttributesModel> attributesModels, String rollback) throws InterruptedException;
}
