package com.Service.Model;

import com.Dao.DataObject.ItemAttributesDO;

import java.math.BigDecimal;

public class ItemAttributesModel {
    private Integer itemattributesid;
    private Integer itemid;
    private String title;
    private BigDecimal price;
    private String imgurl;
    private Integer stock;

    public ItemAttributesModel() {
    }

    public ItemAttributesModel(ItemAttributesDO itemAttributesDO) {
        this.itemid=itemAttributesDO.getItemid();
        this.itemattributesid=itemAttributesDO.getItemattributesid();
        this.title=itemAttributesDO.getTitle();
        this.price=itemAttributesDO.getPrice();
        this.imgurl=itemAttributesDO.getImgurl();
        this.stock=itemAttributesDO.getStock();
    }

    public Integer getItemattributesid() {
        return itemattributesid;
    }

    public void setItemattributesid(Integer itemattributesid) {
        this.itemattributesid = itemattributesid;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
