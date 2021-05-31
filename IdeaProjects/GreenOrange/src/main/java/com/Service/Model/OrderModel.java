package com.Service.Model;

import com.Controller.ViewObject.OrderVo;

import java.math.BigDecimal;
import java.util.ArrayList;

public class OrderModel {
    private String orderid;
    private Integer userid;
    private BigDecimal allprice;
    private BigDecimal finalallprice;
    private ArrayList<OrderAttributesModel> attributesModels=new ArrayList<>();
    private String createdate;
    private String state;
    private String ordernumber;
    private String arrivaladdress;

    public OrderVo getVoFromModel(){
        OrderVo orderVo=new OrderVo(this);
        return orderVo;
    }

    public OrderModel(OrderVo orderVo) {
        this.orderid=orderVo.getOrderid();
        this.userid=orderVo.getUserid();
        this.allprice=orderVo.getAllprice();
        this.finalallprice=orderVo.getFinalallprice();
        this.createdate=orderVo.getCreatedate();
        this.attributesModels=orderVo.getAttributesModels();
        this.state=orderVo.getState();
        this.ordernumber=orderVo.getOrdernumber();
        this.arrivaladdress=orderVo.getArrivaladdress();
    }

    public OrderModel() {
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
