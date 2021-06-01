package com.Dao.DataObject;

import com.Service.Model.OrderAttributesModel;

import java.math.BigDecimal;

public class OrderAttributesDO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orderAttributesInfo.orderAttributesid
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    private Integer orderattributesid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orderAttributesInfo.price
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    private BigDecimal price;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orderAttributesInfo.finalPrice
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    private BigDecimal finalprice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orderAttributesInfo.amount
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    private Integer amount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orderAttributesInfo.orderid
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    private String orderid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orderAttributesInfo.itemAttarbutesid
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    private Integer itemattarbutesid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orderAttributesInfo.evaluateid
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    private Integer evaluateid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orderAttributesInfo.activityid
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    private Integer activityid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orderAttributesInfo.state
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    private String state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orderAttributesInfo.itemid
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    private Integer itemid;
    public OrderAttributesDO(){

    }

    public OrderAttributesDO(OrderAttributesModel orderAttributesDO) {
        this.price=orderAttributesDO.getPrice();
        this.finalprice=orderAttributesDO.getFinalprice();
        this.amount=orderAttributesDO.getAmount();
        this.itemattarbutesid=orderAttributesDO.getItemattarbutesid();
        this.evaluateid=orderAttributesDO.getEvaluateid();
        this.activityid=orderAttributesDO.getActivityid();
        this.state=orderAttributesDO.getState();
        this.itemid=orderAttributesDO.getItemid();
        this.orderid=orderAttributesDO.getOrderid();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orderAttributesInfo.orderAttributesid
     *
     * @return the value of orderAttributesInfo.orderAttributesid
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    public Integer getOrderattributesid() {
        return orderattributesid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orderAttributesInfo.orderAttributesid
     *
     * @param orderattributesid the value for orderAttributesInfo.orderAttributesid
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    public void setOrderattributesid(Integer orderattributesid) {
        this.orderattributesid = orderattributesid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orderAttributesInfo.price
     *
     * @return the value of orderAttributesInfo.price
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orderAttributesInfo.price
     *
     * @param price the value for orderAttributesInfo.price
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orderAttributesInfo.finalPrice
     *
     * @return the value of orderAttributesInfo.finalPrice
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    public BigDecimal getFinalprice() {
        return finalprice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orderAttributesInfo.finalPrice
     *
     * @param finalprice the value for orderAttributesInfo.finalPrice
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    public void setFinalprice(BigDecimal finalprice) {
        this.finalprice = finalprice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orderAttributesInfo.amount
     *
     * @return the value of orderAttributesInfo.amount
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orderAttributesInfo.amount
     *
     * @param amount the value for orderAttributesInfo.amount
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orderAttributesInfo.orderid
     *
     * @return the value of orderAttributesInfo.orderid
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    public String getOrderid() {
        return orderid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orderAttributesInfo.orderid
     *
     * @param orderid the value for orderAttributesInfo.orderid
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orderAttributesInfo.itemAttarbutesid
     *
     * @return the value of orderAttributesInfo.itemAttarbutesid
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    public Integer getItemattarbutesid() {
        return itemattarbutesid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orderAttributesInfo.itemAttarbutesid
     *
     * @param itemattarbutesid the value for orderAttributesInfo.itemAttarbutesid
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    public void setItemattarbutesid(Integer itemattarbutesid) {
        this.itemattarbutesid = itemattarbutesid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orderAttributesInfo.evaluateid
     *
     * @return the value of orderAttributesInfo.evaluateid
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    public Integer getEvaluateid() {
        return evaluateid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orderAttributesInfo.evaluateid
     *
     * @param evaluateid the value for orderAttributesInfo.evaluateid
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    public void setEvaluateid(Integer evaluateid) {
        this.evaluateid = evaluateid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orderAttributesInfo.activityid
     *
     * @return the value of orderAttributesInfo.activityid
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    public Integer getActivityid() {
        return activityid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orderAttributesInfo.activityid
     *
     * @param activityid the value for orderAttributesInfo.activityid
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    public void setActivityid(Integer activityid) {
        this.activityid = activityid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orderAttributesInfo.state
     *
     * @return the value of orderAttributesInfo.state
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    public String getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orderAttributesInfo.state
     *
     * @param state the value for orderAttributesInfo.state
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orderAttributesInfo.itemid
     *
     * @return the value of orderAttributesInfo.itemid
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    public Integer getItemid() {
        return itemid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orderAttributesInfo.itemid
     *
     * @param itemid the value for orderAttributesInfo.itemid
     *
     * @mbg.generated Sun May 23 15:43:17 CST 2021
     */
    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }
}