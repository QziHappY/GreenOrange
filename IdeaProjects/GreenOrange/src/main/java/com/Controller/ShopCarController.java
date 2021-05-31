package com.Controller;

import com.Controller.ReturnType.CommonReturnType;
import com.Service.IService.ItemISerive;
import com.Service.IService.ShopCarIService;
import com.Service.Model.ItemModel;
import com.Service.Model.ShopCarAttributesModel;
import com.Service.Model.ShopCarModel;
import com.Service.ServiceImpl.ItemServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Controller("shopcar")
@RequestMapping("/shopcar")
@CrossOrigin
public class ShopCarController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private ShopCarIService shopCarServiceImpl;
    /**
     * 获取用户购物车信息
     */
    @RequestMapping("/getShopCar")
    @ResponseBody
    public CommonReturnType getShopCar(){
        //用户效验
        String token=httpServletRequest.getHeader("token");
        if(StringUtils.isEmpty(token)||StringUtils.equals(token,"")){
            return CommonReturnType.create("error","用户未登录");
        }
        if(redisTemplate.opsForValue().get(token+"_userid")==null){
            return CommonReturnType.create("error","用户登录失效");
        }
        String id=redisTemplate.opsForValue().get(token+"_userid").toString();
        //redis获取购物车信息
        ShopCarModel shopCarModel=(ShopCarModel)redisTemplate.opsForValue().get("shopCar_"+id);
        //创建新购物车对象
        if(shopCarModel==null){
            shopCarModel=new ShopCarModel(Integer.parseInt(id));
            redisTemplate.opsForValue().set("shopCar_"+id,shopCarModel);//永不过期数据
        }
        return CommonReturnType.create(shopCarModel);
    }
    /**
     * 将某商品加入购物车
     */
    @RequestMapping("/addItemToCar")
    @ResponseBody
    public CommonReturnType addItemToCar(@RequestBody ShopCarAttributesModel shopCarAttributesModel){
        //用户效验
        String token=httpServletRequest.getHeader("token");
        if(StringUtils.isEmpty(token)||StringUtils.equals(token,"")||redisTemplate.opsForValue().get(token+"_userid")==null){
            return CommonReturnType.create("error","用户未登录");
        }
        String userid=redisTemplate.opsForValue().get(token+"_userid").toString();
        if(StringUtils.isEmpty(userid)||StringUtils.equals(userid,"")){
           return CommonReturnType.create("error","用户登录失效");
        }
        int result=shopCarServiceImpl.insertShopCar(userid,shopCarAttributesModel);
        if(result==1){
            return CommonReturnType.create("");
        }else if(result==-1){
            return CommonReturnType.create("error","加入失败，选择了错误的商品属性");
        }else{
            return CommonReturnType.create("error","加入失败");
        }
    }

    /**
     * 将某商品从购物车中移除
     */
    @RequestMapping("/removeFromItemShopCar")
    @ResponseBody
    public CommonReturnType removeFromItemShopCar(@RequestBody ArrayList<Integer> indexlist){
        String token=httpServletRequest.getHeader("token");
        if(StringUtils.isEmpty(token)||StringUtils.equals(token,"")){
            return CommonReturnType.create("error","未登录");
        }
        String id=redisTemplate.opsForValue().get(token+"_userid").toString();
        if(StringUtils.isEmpty("id")|StringUtils.equals(id,"")){
            return CommonReturnType.create("error","登录失效");
        }
        ShopCarModel shopCarModel=(ShopCarModel)redisTemplate.opsForValue().get("shopCar_"+id);
        ArrayList<ShopCarAttributesModel> list = shopCarModel.getList();
        for(int i=indexlist.size()-1;i>=0;i--){
            list.remove((int)indexlist.get(i));
        }
        shopCarModel.setList(list);
        redisTemplate.opsForValue().set("shopCar_"+id,shopCarModel);
        return CommonReturnType.create("");
    }
    /**
     * 修改某商品的属性
     */
    @RequestMapping("/updateItemFromShopCar")
    @ResponseBody
    public CommonReturnType updateItemFromShopCar(){
        return CommonReturnType.create("");
    }
}
