package com.Service.ServiceImpl;

import com.Dao.DataObject.UserDO;
import com.Dao.DataObject.UserPasswordDO;
import com.Dao.UserDOMapper;
import com.Dao.UserPasswordDOMapper;
import com.Service.IService.UserIService;
import com.Service.Model.UserModel;
import com.Utils.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserIService {

    @Autowired
    UserDOMapper userDOMapper;
    @Autowired
    UserPasswordDOMapper userPasswordDOMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    @Transactional
    public int resign(UserModel userModel) {
        try{
            UserDO userDO= new UserDO();
            BeanUtils.copyProperties(userModel,userDO);
            userDOMapper.insert(userDO);
            int userid=userDO.getUserid();// 成功返回userid
            UserPasswordDO userPasswordDO=new UserPasswordDO();
            String pd=userModel.getEncrptPassword();
            String encryptpd=pd;//加密
            userPasswordDO.setEncrptPassword(encryptpd);
            userPasswordDO.setUserid(userid);
            userPasswordDOMapper.insert(userPasswordDO);
            return userid;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void updateUserInfo(UserModel userModel) {
        UserDO userDO=new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        userDOMapper.updateByPrimaryKey(userDO);
    }

    @Override
    public void updateUserPassword(int userid, String password) {
        UserPasswordDO userPasswordDO=userPasswordDOMapper.selectByUserid(userid);
        userPasswordDO.setEncrptPassword(password);
        userPasswordDOMapper.updateByPrimaryKey(userPasswordDO);
    }

    @Override
    public UserModel getUserModelByUserid(int userid) {
        //获取用户个人信息
        UserDO userDO=userDOMapper.selectByPrimaryKey(userid);
        UserModel userModel=new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        return userModel;
    }

    @Override
    public UserModel getUserModelByTelphone(String telphone) {
        UserModel userModel=new UserModel();
        UserDO userDO=userDOMapper.selectByTelphone(telphone);
        if(userDO==null){
            return null;
        }
        UserPasswordDO userPasswordDO=userPasswordDOMapper.selectByUserid(userDO.getUserid());
        BeanUtils.copyProperties(userDO,userModel);
        userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        return userModel;
    }
    //userid或telphone,opt绑定token
    @Transactional
    public String setToken(int userid,String telphone,String opt) {
        String token= JwtUtil.setToken();//生成toke
        //注册/验证码登录:绑定telphone和opt
        if(userid==-1){
            redisTemplate.opsForValue().set(token+"_telphone",telphone,3,TimeUnit.MINUTES);
            redisTemplate.opsForValue().set(token+"_opt",opt,3,TimeUnit.MINUTES);
        }
        //登录：绑定userid
        else{
            redisTemplate.opsForValue().set(token+"_userid",String.valueOf(userid),1,TimeUnit.HOURS);
        }
        return token;
    }
}
