package com.Service.Model;

import com.Controller.ViewObject.ItemVo;
import com.Dao.DataObject.ItemDO;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ItemModel {
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

    public ItemModel() {
    }

    public ItemModel(ItemDO itemDO){
        this.itemid=itemDO.getItemid();
        this.title=itemDO.getTitle();
        this.descrption=itemDO.getDescrption();
        this.sales=itemDO.getSales();
        this.brand=itemDO.getBrand();
        this.type = itemDO.getType();
        this.imgurl=itemDO.getImgurl();
        this.maxprice=itemDO.getMaxprice();
        this.minprice=itemDO.getMinprice();
        this.activityid=itemDO.getActivityid();
    }
    public ItemModel(ItemVo itemVo) {
        this.itemid=itemVo.getItemid();
        this.title=itemVo.getTitle();
        this.descrption=itemVo.getDescrption();
        this.sales=itemVo.getSales();
        this.brand=itemVo.getBrand();
        this.type = itemVo.getType();
        this.imgurl=itemVo.getImgurl();
        BigDecimal max=itemVo.getItemAttributesList().get(0).getPrice();
        BigDecimal min=max;
        for(int i=0;i<itemVo.getItemAttributesList().size();i++){
            this.itemAttributesList.add(itemVo.getItemAttributesList().get(i));
            if(max.compareTo(itemVo.getItemAttributesList().get(i).getPrice())==-1){
                max=itemVo.getItemAttributesList().get(i).getPrice();
            }
            if(min.compareTo(itemVo.getItemAttributesList().get(i).getPrice())==1){
                min=itemVo.getItemAttributesList().get(i).getPrice();
            }
        }
        this.maxprice=max;
        this.minprice=min;
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
