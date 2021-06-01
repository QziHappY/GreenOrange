package com.Controller;
import com.Controller.ReturnType.CommonReturnType;
import com.Controller.ViewObject.UserVo;
import com.Service.Model.AddressModel;
import com.Service.Model.UserModel;
import com.Service.ServiceImpl.UserServiceImpl;
import com.Utils.Base64Utils;
import com.Utils.Opt;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.jvm.hotspot.debugger.Address;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     （1）用户通过手机号注册账号-1：验证手机号、发送验证码（type="resign"）
     （2）用户通过验证码登录账号-1（type="login"）
     （3）用户通过验证码修改密码-1（type="updatePassword"）
     */
    @RequestMapping("/toOpt")
    @ResponseBody
    public CommonReturnType toOpt(@RequestParam("telphone")String telphone, @RequestParam("type")String type,@RequestParam(value="token",required=false)String token_){
        //仅当修改密码时：登录验证并获取telphone
        if(StringUtils.equals(type,"updatePassword")){
            if(StringUtils.equals(token_,"")||StringUtils.isEmpty(token_)){
                return CommonReturnType.create("error","未登录");
            }
            UserModel userModel = (UserModel)redisTemplate.opsForValue().get(token_+"_userid");
            telphone=userModel.getTelphone();
        }
        //验证是否为空或有效
        if(StringUtils.isEmpty(telphone)||StringUtils.equals(telphone,"")){
            return CommonReturnType.create("error","手机号为空");
        }
        //注册or登录 效验
        UserModel userModel=userServiceImpl.getUserModelByTelphone(telphone);
        if(StringUtils.equals(type,"resign")){
            if(userModel!=null){
                return CommonReturnType.create("error","已经注册过了");
            }
        }else{
            if(userModel==null){
                return CommonReturnType.create("error","该账号还未注册");
            }
        }
        //生成7位验证码
        String opt= Opt.setOpt(7);
        //发送验证码
        if(Opt.sendOpt(opt,telphone,type)==0){
            return CommonReturnType.create("error","发送验证码失败");
        }
        //验证码与手机号绑定token 有效期：3min
        String token=userServiceImpl.setToken(-1,telphone,opt);

        Map<String,String> map=new HashMap<>();
        map.put("token",token);
        map.put("opt",opt);
        return CommonReturnType.create(map);
        //返回成功
        //return CommonReturnType.create(token);
    }

    /**
     用户通过手机号注册账号-2：提交验证码、新密码注册用户
     */
    @RequestMapping("/resign")
    @ResponseBody
    public CommonReturnType resign(@RequestParam("token")String token,@RequestParam("opt")String opt, @RequestParam("encrypt_password")String encrypt_password) {
        //验证是否为空
        if(StringUtils.isEmpty(opt)||StringUtils.equals(opt,"")){
            return CommonReturnType.create("error","验证码为空");
        }
        if(StringUtils.isEmpty(encrypt_password)||StringUtils.equals(encrypt_password,"")){
            return CommonReturnType.create("error","密码为空");
        }
        if(StringUtils.isEmpty(token)||StringUtils.equals(token,"")){
            return CommonReturnType.create("error","无效注册");
        }
        //取出绑定的验证码与手机号
        String telphone=redisTemplate.opsForValue().get(token+"_telphone").toString();
        String right_opt=redisTemplate.opsForValue().get(token+"_opt").toString();
        //验证是否过期
        if(StringUtils.equals(telphone,"")||StringUtils.equals(right_opt,"")){
            return CommonReturnType.create("error","验证码已经过期了");
        }
        //验证是否正确
        if(!StringUtils.equals(opt,right_opt)){
            return CommonReturnType.create("error","验证码不正确");
        }
        //建立新用户，填入默认信息
        UserModel userModel=new UserModel();
        userModel.setTelphone(telphone);
        userModel.init();
        //为新用户设置新密码
        String pd=encrypt_password;
        userModel.setEncrptPassword(Base64Utils.encode(pd));//Base64加密入库
        int userid=userServiceImpl.resign(userModel);
        //登录新用户
        redisTemplate.opsForValue().set(token+"_userid",String.valueOf(userid),1,TimeUnit.HOURS);
        redisTemplate.opsForValue().set("user_"+userid,userModel,1,TimeUnit.HOURS);
        //返回成功
        return CommonReturnType.create("");
    }
    /**
     用户通过验证码登录账号-2
     */
    @RequestMapping("/loginByOpt")
    @ResponseBody
    public CommonReturnType  loginByOpt(@RequestParam("opt") String opt,@RequestParam("token") String token){
        //验证是否为空
        if(StringUtils.isEmpty(opt)||StringUtils.equals(opt,"")){
            return CommonReturnType.create("error","验证码为空");
        }
        if(StringUtils.isEmpty(token)||StringUtils.equals(token,"")){
            return CommonReturnType.create("error","无效注册");
        }
        //取出绑定的验证码与手机号
        String telphone=redisTemplate.opsForValue().get(token+"_telphone").toString();
        String right_opt=redisTemplate.opsForValue().get(token+"_opt").toString();
        //验证是否过期
        if(StringUtils.equals(telphone,"")||StringUtils.equals(right_opt,"")){
            return CommonReturnType.create("error","验证码已经过期了");
        }
        //验证是否正确
        if(!StringUtils.equals(opt,right_opt)){
            return CommonReturnType.create("error","验证码不正确");
        }
        //登录成功
        UserModel userModel=userServiceImpl.getUserModelByTelphone(telphone);
        //将可能存在的已经登录的用户挤下来:更换user的有效token
        //redisTemplate.opsForValue().set(String.valueOf(userModel.getUserid())+"_user_token",token,1,TimeUnit.HOURS);
        //将有效token绑定userid
        redisTemplate.opsForValue().set(token+"_userid",String.valueOf(userModel.getUserid()),1, TimeUnit.HOURS);
        //userid绑定usermodel
        redisTemplate.opsForValue().set("user_"+userModel.getUserid(),userModel,1, TimeUnit.HOURS);
        //返回成功
        return CommonReturnType.create("");
    }

    /**
    用户通过密码登录账号
     */
    @RequestMapping("/loginByPassword")
    @ResponseBody
    public CommonReturnType  loginByPassword(@RequestParam("telphone") String telphone, @RequestParam("entrypt_password") String entrypt_password){
        UserModel userModel=userServiceImpl.getUserModelByTelphone(telphone);
        if(userModel==null){
            return CommonReturnType.create("error","还没有注册过");
        }
        String password_1=Base64Utils.decode(userModel.getEncrptPassword());//Base64解密
        String password_2=entrypt_password;//前端解密
        //验证密码
        if(StringUtils.equals(password_1,password_2)){
            String token=userServiceImpl.setToken(userModel.getUserid(),"","");//生成token并与userid绑定
            redisTemplate.opsForValue().set("user_"+userModel.getUserid(),userModel,1,TimeUnit.HOURS);
            return CommonReturnType.create(token);
        }else{
            return CommonReturnType.create("error","密码错误");
        }
    }
    /**
     * 查看用户个人信息
     */
    @RequestMapping("/getUserInfo")
    @ResponseBody
    public CommonReturnType getUserInfo(@RequestParam("token")String token){
        //查看用户是否登录
        if(StringUtils.isEmpty(token)||StringUtils.equals(token,"")||redisTemplate.opsForValue().get(token+"_userid")==null){
            return CommonReturnType.create("error","未登录");
        }
        String userid=redisTemplate.opsForValue().get(token+"_userid").toString();
        if(StringUtils.isEmpty(userid)||StringUtils.equals(userid,"")){
            return CommonReturnType.create("error","登录已失效");
        }
        UserModel userModel= (UserModel)redisTemplate.opsForValue().get("user_"+userid);
        UserVo userVo=new UserVo(userModel);
        return CommonReturnType.create(userVo);
    }
    /**
    用户信息修改-1:用户基础信息修改
     */
    @RequestMapping( "/updateUserInfo")
    @ResponseBody
    public CommonReturnType  updateUserInfo(@RequestParam("token")String token,@RequestParam("name")String name, @RequestParam("sex")String sex, @RequestParam("birthdate")String birthdate){
        //查看用户是否登录
        if(StringUtils.isEmpty(token)||StringUtils.equals(token,"")){
            return CommonReturnType.create("error","未登录");
        }
        String userid=redisTemplate.opsForValue().get(token+"_userid").toString();
        if(StringUtils.isEmpty(userid)||StringUtils.equals(userid,"")){
            return CommonReturnType.create("error","登录已失效");
        }
        //获取信息
        UserModel userModel=(UserModel)redisTemplate.opsForValue().get("user_"+userid);
        userModel.setSex(sex);
        userModel.setUsername(name);
        userModel.setBirthdate(birthdate);
        //更新信息到缓存和数据库中
        redisTemplate.opsForValue().set("userid_"+userid,userModel,1,TimeUnit.HOURS);
        userServiceImpl.updateUserInfo(userModel);
        return CommonReturnType.create("");
    }
    /**
    用户信息修改-2：通过验证码修改密码-2
     */
    @RequestMapping("/updateUserPassword")
    @ResponseBody
    public CommonReturnType updateUserPassword(@RequestParam("token")String token,@RequestParam("opt")String opt, @RequestParam("encrypt_password")String encrypt_password) {
        //验证验证码是否为空
        if(StringUtils.isEmpty(opt)||StringUtils.equals(opt,"")){
            return CommonReturnType.create("error","验证码为空");
        }
        if(StringUtils.isEmpty(encrypt_password)||StringUtils.equals(encrypt_password,"")){
            return CommonReturnType.create("error","密码为空");
        }
        //查看用户是否登录
        if(StringUtils.isEmpty(token)||StringUtils.equals(token,"")){
            return CommonReturnType.create("error","未登录");
        }
        String userid_=redisTemplate.opsForValue().get(token+"_userid").toString();
        if(StringUtils.isEmpty(userid_)||StringUtils.equals(userid_,"")){
            return CommonReturnType.create("error","登录已失效");
        }

        //取出绑定的验证码与手机号
        String right_opt=this.httpServletRequest.getSession().getAttribute("opt").toString();
        if(!StringUtils.equals(opt,right_opt)){
            return CommonReturnType.create("error","验证码不正确");
        }
        //清除session
        this.httpServletRequest.getSession().removeAttribute("opt");
        //获取userid
        String suid=this.httpServletRequest.getSession().getAttribute("userid").toString();
        if(StringUtils.equals(suid,"")||StringUtils.isEmpty(suid)){
            return CommonReturnType.create("error","用户未登录");
        }
        int userid=Integer.valueOf(suid);
        //为用户设置新密码
        String pd=encrypt_password;//前端解密
        String encrypt_pd=Base64Utils.encode(pd);//后端加密
        userServiceImpl.updateUserPassword(userid,encrypt_pd);
        //返回成功
        return CommonReturnType.create("");
    }
    /**
     * 用户退出账号
     */
    @RequestMapping("/cancel")
    @ResponseBody
    public CommonReturnType cancel(@RequestParam("token")String token){
        String userid=redisTemplate.opsForValue().get(token+"_userid").toString();
        redisTemplate.delete(token+"_userid");
        redisTemplate.delete("userid_"+userid);
        return CommonReturnType.create("");
    }


    /**
     * 查看用户的地址信息列表
     */
    @RequestMapping("/getAddressList")
    @ResponseBody
    public CommonReturnType getAddressList(@RequestParam("token")String token){
        //查看用户是否登录
        if(StringUtils.isEmpty(token)||StringUtils.equals(token,"")){
            return CommonReturnType.create("error","未登录");
        }
        String userid_=redisTemplate.opsForValue().get(token+"_userid").toString();
        if(StringUtils.isEmpty(userid_)||StringUtils.equals(userid_,"")){
            return CommonReturnType.create("error","登录已失效");
        }
        ArrayList<AddressModel> addressModels=userServiceImpl.getAddressList(Integer.parseInt(userid_));
        return CommonReturnType.create(addressModels);
    }
    /**
     * 查看地址详细信息
     */
    @RequestMapping("/getAddress")
    @ResponseBody
    public CommonReturnType getAddress(String addressid){
        String token=httpServletRequest.getHeader("token");
        //查看用户是否登录
        if(StringUtils.isEmpty(token)||StringUtils.equals(token,"")){
            return CommonReturnType.create("error","未登录");
        }
        String userid_=redisTemplate.opsForValue().get(token+"_userid").toString();
        if(StringUtils.isEmpty(userid_)||StringUtils.equals(userid_,"")){
            return CommonReturnType.create("error","登录已失效");
        }
        AddressModel addressModel=userServiceImpl.getAddressById(addressid);
        return CommonReturnType.create(addressModel);
    }
    /**
     * 修改地址信息
     */
    @RequestMapping("/updateAddress")
    @ResponseBody
    public CommonReturnType getAddress(AddressModel model){
        String token=httpServletRequest.getHeader("token");
        //查看用户是否登录
        if(StringUtils.isEmpty(token)||StringUtils.equals(token,"")){
            return CommonReturnType.create("error","未登录");
        }
        String userid_=redisTemplate.opsForValue().get(token+"_userid").toString();
        if(StringUtils.isEmpty(userid_)||StringUtils.equals(userid_,"")){
            return CommonReturnType.create("error","登录已失效");
        }
        int result=userServiceImpl.updateAddress(model);
        if(result>=1){
            return CommonReturnType.create("");
        }else{
            return CommonReturnType.create("error","修改失败");
        }
    }
    /**
     * 删除地址信息
     */
    @RequestMapping("/deleteAddress")
    @ResponseBody
    public CommonReturnType deleteAddress(int addressid){
        String token=httpServletRequest.getHeader("token");
        //查看用户是否登录
        if(StringUtils.isEmpty(token)||StringUtils.equals(token,"")){
            return CommonReturnType.create("error","未登录");
        }
        String userid_=redisTemplate.opsForValue().get(token+"_userid").toString();
        if(StringUtils.isEmpty(userid_)||StringUtils.equals(userid_,"")){
            return CommonReturnType.create("error","登录已失效");
        }
        int result=userServiceImpl.deleteAddress(addressid);
        if(result>=1){
            return CommonReturnType.create("");
        }else{
            return CommonReturnType.create("error","删除失败");
        }
    }
    /**
     * 新增地址信息
     */
    @RequestMapping("/addAddress")
    @ResponseBody
    public CommonReturnType deleteAddress(AddressModel model){
        String token=httpServletRequest.getHeader("token");
        //查看用户是否登录
        if(StringUtils.isEmpty(token)||StringUtils.equals(token,"")){
            return CommonReturnType.create("error","未登录");
        }
        String userid_=redisTemplate.opsForValue().get(token+"_userid").toString();
        if(StringUtils.isEmpty(userid_)||StringUtils.equals(userid_,"")){
            return CommonReturnType.create("error","登录已失效");
        }
        int result=userServiceImpl.addAddress(model);
        if(result>=1){
            return CommonReturnType.create("");
        }else{
            return CommonReturnType.create("error","新增失败");
        }
    }
}
