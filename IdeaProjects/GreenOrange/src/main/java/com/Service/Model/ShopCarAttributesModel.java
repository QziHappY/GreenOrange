package com.Service.Model;

import java.math.BigDecimal;

public class ShopCarAttributesModel {
    private int itemid;
    private int itemAttributesid;
    private int amount;
    private BigDecimal price;
    private int activityid;

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public int getItemAttributesid() {
        return itemAttributesid;
    }

    public void setItemAttributesid(int itemAttributesid) {
        this.itemAttributesid = itemAttributesid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getActivityid() {
        return activityid;
    }

    public void setActivityid(int activityid) {
        this.activityid = activityid;
    }
}
