package com.Controller.ViewObject;

import com.Service.Model.UserModel;

import java.text.DateFormat;
import java.util.Date;

public class UserVo {
    private int userid;
    private String name;
    private String telphone;
    private String birthdate;
    private String sex;
    private String identity;
    private String imgUrl;
    //setVoFromModel()
    public UserVo(UserModel model) {
        this.userid = model.getUserid();
        this.name = model.getUsername();
        this.telphone = model.getTelphone();
        this.birthdate = model.getBirthdate();
        this.sex = model.getSex();
        this.identity = model.getIdentity();
        this.imgUrl=model.getImgUrl();
    }
    //getModelFromVo()
    public UserModel getModelFromVo(UserVo userVo){
        UserModel userModel=new UserModel();
        userModel.setUserid(userVo.getUserid());
        userModel.setTelphone(userVo.getTelphone());
        userModel.setSex(userVo.getSex());
        userModel.setUsername(userVo.getName());
        userModel.setIdentity(userVo.getIdentity());
        userModel.setBirthdate(userVo.getBirthdate());
        userModel.setImgUrl(userVo.getImgUrl());
        return userModel;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public UserVo(){

    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
