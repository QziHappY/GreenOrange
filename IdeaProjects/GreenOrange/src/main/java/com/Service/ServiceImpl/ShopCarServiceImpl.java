package com.Service.ServiceImpl;

import com.Controller.ReturnType.CommonReturnType;
import com.Service.IService.ItemISerive;
import com.Service.IService.ShopCarIService;
import com.Service.Model.ItemModel;
import com.Service.Model.ShopCarAttributesModel;
import com.Service.Model.ShopCarModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;
@Service
public class ShopCarServiceImpl implements ShopCarIService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ItemISerive itemServiceImpl;
    public int insertShopCar(String userid, ShopCarAttributesModel shopCarAttributesModel){
        //redis获取购物车信息
        ShopCarModel shopCarModel=(ShopCarModel)redisTemplate.opsForValue().get("shopCar_"+userid);
        if(shopCarModel==null){
            shopCarModel=new ShopCarModel(Integer.parseInt(userid));
        }
        //查看最新的活动和价格
        ItemModel itemModel=(ItemModel)redisTemplate.opsForValue().get("item_"+shopCarAttributesModel.getItemid());
        //缓存未命中
        if(itemModel==null){
            itemModel= itemServiceImpl.getItemById(shopCarAttributesModel.getItemid());
            redisTemplate.opsForValue().set("item_"+itemModel.getItemid(),itemModel,1, TimeUnit.HOURS);
        }
        //使用最新活动id
        if(itemModel.getActivityid()==null){
            shopCarAttributesModel.setActivityid(-1);
        }else{
            shopCarAttributesModel.setActivityid(itemModel.getActivityid());
        }
        int flag=0;
        //使用最新价格
        for(int j=0;j<itemModel.getItemAttributesList().size();j++){
            if(itemModel.getItemAttributesList().get(j).getItemattributesid()==shopCarAttributesModel.getItemAttributesid()){
                shopCarAttributesModel.setPrice(itemModel.getItemAttributesList().get(j).getPrice());
                flag=1;
                break;
            }
        }
        if(flag==0){
            return -1;
        }
        //是否会与购物车中的商品合并
        for(int i=0;i<shopCarModel.getList().size();i++){
            //满足合并条件：同一件商品的同一属性的件数
            if(shopCarAttributesModel.getItemid()==shopCarModel.getList().get(i).getItemid()&&
                    shopCarAttributesModel.getItemAttributesid()==shopCarModel.getList().get(i).getItemAttributesid()){
                shopCarModel.getList().get(i).setAmount(shopCarModel.getList().get(i).getAmount()+shopCarAttributesModel.getAmount());
                redisTemplate.opsForValue().set("shopCar_"+userid,shopCarModel);
                return 1;
            }
        }
        //未能合并：加入到购物车中
        shopCarModel.add(shopCarAttributesModel);
        redisTemplate.opsForValue().set("shopCar_"+userid,shopCarModel);
        return 1;
    }
}
