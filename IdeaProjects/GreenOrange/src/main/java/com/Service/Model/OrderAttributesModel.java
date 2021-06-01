package com.Service.Model;

import java.math.BigDecimal;

public class OrderAttributesModel {
    private Integer orderattributesid;
    private BigDecimal price;
    private BigDecimal finalprice;
    private Integer amount;
    private String orderid;
    private Integer itemattarbutesid;
    private Integer evaluateid;
    private Integer activityid;
    private String state;
    private Integer itemid;

    public OrderAttributesModel() {
    }

    public Integer getOrderattributesid() {
        return orderattributesid;
    }

    public void setOrderattributesid(Integer orderattributesid) {
        this.orderattributesid = orderattributesid;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getFinalprice() {
        return finalprice;
    }

    public void setFinalprice(BigDecimal finalprice) {
        this.finalprice = finalprice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public Integer getItemattarbutesid() {
        return itemattarbutesid;
    }

    public void setItemattarbutesid(Integer itemattarbutesid) {
        this.itemattarbutesid = itemattarbutesid;
    }

    public Integer getEvaluateid() {
        return evaluateid;
    }

    public void setEvaluateid(Integer evaluateid) {
        this.evaluateid = evaluateid;
    }

    public Integer getActivityid() {
        return activityid;
    }

    public void setActivityid(Integer activityid) {
        this.activityid = activityid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }
}
