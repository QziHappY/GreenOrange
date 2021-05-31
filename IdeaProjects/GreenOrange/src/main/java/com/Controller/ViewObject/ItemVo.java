package com.Controller.ViewObject;

import com.Service.Model.ItemAttributesModel;
import com.Service.Model.ItemModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;

public class ItemVo {
    private Integer itemid;
    private String title;
    private String descrption;
    private Integer sales;
    private String brand;
    private String type;
    private String imgurl;
    private BigDecimal maxprice;
    private BigDecimal minprice;
    private ArrayList<ItemAttributesModel> itemAttributesList=new ArrayList<>();
    private Integer activityid;

    public ItemVo(){

    }
    public ItemVo(ItemModel itemModel) {
        this.itemid=itemModel.getItemid();
        this.title=itemModel.getTitle();
        this.descrption=itemModel.getDescrption();
        this.sales=itemModel.getSales();
        this.brand=itemModel.getBrand();
        this.type = itemModel.getType();
        this.imgurl=itemModel.getImgurl();
        for(int i=0;i<itemModel.getItemAttributesList().size();i++){
            this.itemAttributesList.add(itemModel.getItemAttributesList().get(i));
        }
        this.maxprice=itemModel.getMaxprice();
        this.minprice=itemModel.getMinprice();
    }


    public ArrayList<ItemAttributesModel> getItemAttributesList() {
        return itemAttributesList;
    }

    public void setItemAttributesList(ArrayList<ItemAttributesModel> itemAttributesList) {
        this.itemAttributesList = itemAttributesList;
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

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public BigDecimal getMaxprice() {
        return maxprice;
    }

    public void setMaxprice(BigDecimal maxprice) {
        this.maxprice = maxprice;
    }

    public BigDecimal getMinprice() {
        return minprice;
    }

    public void setMinprice(BigDecimal minprice) {
        this.minprice = minprice;
    }

    public Integer getActivityid() {
        return activityid;
    }

    public void setActivityid(Integer activityid) {
        this.activityid = activityid;
    }
}