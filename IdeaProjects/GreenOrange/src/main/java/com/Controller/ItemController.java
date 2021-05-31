package com.Controller;

import com.Controller.ReturnType.CommonReturnType;
import com.Controller.ViewObject.ItemVo;
import com.Service.IService.BrandIService;
import com.Service.IService.ItemISerive;
import com.Service.IService.TypeIService;
import com.Service.Model.ItemModel;
import com.Service.ServiceImpl.TypeServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.tree.ReturnType;
import sun.tools.jconsole.inspector.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Controller("item")
@RequestMapping("/item")
@CrossOrigin
public class ItemController {

    @Autowired
    ItemISerive itemServiceImpl;
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 单一条件查询-获取商品列表
     */
    @RequestMapping("/getItemListByCondition")
    @ResponseBody
    public CommonReturnType getItemListByCondition(@RequestParam("condition")String condition,
                                        @RequestParam("condition_value")String condition_value,
                                        @RequestParam("page")int page,
                                        @RequestParam("limit")int limit){
        ArrayList<ItemModel> itemModels=itemServiceImpl.getItemListByCondition(condition,condition_value,page,limit);
        return CommonReturnType.create(itemModels);
    }
    /**
     * 多条件查询-获取商品列表
     */
    @RequestMapping("/getItemList")
    @ResponseBody
    public CommonReturnType getItemList(@RequestParam("title")String title,
                                        @RequestParam("brand")String brand,
                                        @RequestParam("type")String type,
                                        @RequestParam("page")int page,
                                        @RequestParam("limit")int limit){
        ArrayList<ItemModel> itemModels=itemServiceImpl.getItemList(title,brand,type,page,limit);
        return CommonReturnType.create(itemModels);
    }

    /**
     （1）添加单个商品
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public CommonReturnType  addItem(@RequestBody ItemVo itemVo){
        try {
            ItemModel itemModel=new ItemModel(itemVo);
            itemServiceImpl.addItem(itemModel);
            return CommonReturnType.create("");
        }catch (Exception e){
            e.printStackTrace();
            return CommonReturnType.create("error","");
        }
    }

    /**
     （2）批量添加商品
     */
    @RequestMapping("/addItems")
    @ResponseBody
    public CommonReturnType  addItems(){
        HashMap<String,String> hashMap=new HashMap<>();
//        for(){
//            ItemModel itemModel=new ItemModel();
//            itemServiceImpl.addItem(itemModel);
//
//        }
        if(hashMap.size()!=0){
            return CommonReturnType.create(hashMap);
        }
        return CommonReturnType.create("");
    }

    /**
     （1）删除单个商品
     */
    @RequestMapping("/deleteItem")
    @ResponseBody
    public CommonReturnType deleteItem(@RequestParam("itemid") Integer itemid){
        try{
            redisTemplate.delete("item_"+itemid);//删除缓存中的商品
            int result=itemServiceImpl.deleteItem(itemid);
            if(result>0){
                return CommonReturnType.create("");
            }else{
                return CommonReturnType.create("error","删除失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return CommonReturnType.create("error","删除失败");
        }
    }
    /**
     （2）批量删除商品
     */
    @RequestMapping("/deleteItems")
    @ResponseBody
    public CommonReturnType deleteItems(@RequestBody ArrayList<Integer> ids){
        try{
            int result=0;
            for(int i=0;i<ids.size();i++){
                redisTemplate.delete("item_"+ids.get(i));//删除缓存中的商品
                result+=itemServiceImpl.deleteItem(ids.get(i));
            }
            if(result==ids.size())
                return CommonReturnType.create("");
            else
                return CommonReturnType.create("error","部分失败");
        }catch (Exception e){
            e.printStackTrace();
            return CommonReturnType.create("error","删除失败");
        }
    }

    /**
     查看商品信息
     */
    @RequestMapping("/getItem")
    @ResponseBody
    public CommonReturnType getItem(@RequestParam("itemid")Integer itemid){
        ItemModel itemModel=(ItemModel)redisTemplate.opsForValue().get("item_"+itemid);
        //缓存未命中
        if(itemModel==null){
            itemModel=itemServiceImpl.getItemById(itemid);
            redisTemplate.opsForValue().set("item_"+itemModel.getItemid(),itemModel,1, TimeUnit.HOURS);
        }
        //数据库未命中
        if(itemModel==null){
            return CommonReturnType.create("error","查无此商品");
        }
        ItemVo itemVo=new ItemVo(itemModel);
        return CommonReturnType.create(itemVo);
    }


    /**
        修改商品信息
     */
    @RequestMapping("/updateItem")
    @ResponseBody
    public CommonReturnType  updateItem(@RequestBody ItemVo itemVo){
        try {
            ItemModel itemModel=new ItemModel(itemVo);
            //修改redis中的商品
            redisTemplate.delete("item_"+itemModel.getItemid());
            redisTemplate.opsForValue().set("item_"+itemModel.getItemid(),itemModel,1, TimeUnit.HOURS);
            //修改数据库
            int result=itemServiceImpl.updateItem(itemModel);
            if(result>0){
                return CommonReturnType.create("");
            }else{
                return CommonReturnType.create("error","");
            }
        }catch (Exception e){
            e.printStackTrace();
            return CommonReturnType.create("error","");
        }
    }
}
