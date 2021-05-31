package com.Service.Model;

import java.util.ArrayList;

public class ShopCarModel {

    private int userid;
    private ArrayList<ShopCarAttributesModel> list;

    public ShopCarModel() {
    }

    public ShopCarModel(int userid) {
        this.userid=userid;
        list=new ArrayList<>();
    }

    //加入购物车
    public int add(ShopCarAttributesModel shopCarAttributesModel){
        list.add(shopCarAttributesModel);
        return 1;
    }
    //从购物车中删除
    public int delete(int i){
        list.remove(i);
        return 1;
    }
    //修改购物车
    public int update(int i, int itemAttributesid,int amount){
        list.get(i).setItemAttributesid(itemAttributesid);
        list.get(i).setAmount(amount);
        return 1;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public ArrayList<ShopCarAttributesModel> getList() {
        return list;
    }

    public void setList(ArrayList<ShopCarAttributesModel> list) {
        this.list = list;
    }
}
