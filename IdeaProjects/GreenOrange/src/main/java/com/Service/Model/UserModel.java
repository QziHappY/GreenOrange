package com.Service.Model;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
public class UserModel {
    private int userid;
    private String username;
    private String encrptPassword;
    private String telphone;
    private String birthdate;
    private String sex;
    private String identity;
    private String imgUrl;
    public void init(){
        this.username= RandomStringUtils.random(10,true,true);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        this.birthdate=sdf.format(new Date());
        this.sex="男";
        this.identity="普通用户";
        this.imgUrl="";
    }

    public UserModel() {
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getEncrptPassword() {
        return encrptPassword;
    }

    public void setEncrptPassword(String encrptPassword) {
        this.encrptPassword = encrptPassword;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
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
