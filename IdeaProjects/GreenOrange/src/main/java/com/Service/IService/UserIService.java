package com.Service.IService;

import com.Service.Model.AddressModel;
import com.Service.Model.UserModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

public interface UserIService {
    //注册新用户：返回userid
    public int resign(UserModel userModel);

    //修改用户个人基础信息
    public void updateUserInfo(UserModel userModel);

    //修改用户密码
    public void updateUserPassword(int userid,String password);

    //根据用户id获取用户信息
    public UserModel getUserModelByUserid(int userid);

    //根据用户手机号获取用户信息
    public UserModel getUserModelByTelphone(String telphone);

    int addAddress(AddressModel model);

    int deleteAddress(int addressid);

    int updateAddress(AddressModel model);

    AddressModel getAddressById(String addressid);

    ArrayList<AddressModel> getAddressList(int userid);
}
