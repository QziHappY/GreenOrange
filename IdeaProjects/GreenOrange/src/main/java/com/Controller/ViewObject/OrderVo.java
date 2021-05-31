package com.Controller.ViewObject;

import com.Dao.DataObject.OrderDO;
import com.Service.Model.OrderAttributesModel;
import com.Service.Model.OrderModel;

import java.math.BigDecimal;
import java.util.ArrayList;

public class OrderVo {

    private String  orderid;
    private Integer userid;
    private BigDecimal allprice;
    private BigDecimal finalallprice;
    private String createdate;
    private String state;
    private ArrayList<OrderAttributesModel> attributesModels=new ArrayList<>();
    private String ordernumber;
    private String arrivaladdress;

    public OrderVo() {
    }

    public OrderVo(OrderModel orderModel) {
        this.orderid=orderModel.getOrderid();
        this.userid=orderModel.getUserid();
        this.allprice=orderModel.getAllprice();
        this.finalallprice=orderModel.getFinalallprice();
        this.createdate=orderModel.getCreatedate();
        this.attributesModels=orderModel.getAttributesModels();
        this.state=orderModel.getState();
        this.ordernumber=orderModel.getOrdernumber();
        this.arrivaladdress=orderModel.getArrivaladdress();
    }

    public OrderVo(OrderDO orderDO) {
        this.orderid=orderDO.getOrderid();
        this.userid=orderDO.getUserid();
        this.allprice=orderDO.getAllprice();
        this.finalallprice=orderDO.getFinalallprice();
        this.createdate=orderDO.getCreatedate();
        this.state=orderDO.getState();
        this.ordernumber=orderDO.getOrdernumber();
        this.arrivaladdress=orderDO.getArrivaladdress();
    }

    public ArrayList<OrderAttributesModel> getAttributesModels() {
        return attributesModels;
    }

    public void setAttributesModels(ArrayList<OrderAttributesModel> attributesModels) {
        this.attributesModels = attributesModels;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public BigDecimal getAllprice() {
        return allprice;
    }

    public void setAllprice(BigDecimal allprice) {
        this.allprice = allprice;
    }

    public BigDecimal getFinalallprice() {
        return finalallprice;
    }

    public void setFinalallprice(BigDecimal finalallprice) {
        this.finalallprice = finalallprice;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public String getArrivaladdress() {
        return arrivaladdress;
    }

    public void setArrivaladdress(String arrivaladdress) {
        this.arrivaladdress = arrivaladdress;
    }
}
