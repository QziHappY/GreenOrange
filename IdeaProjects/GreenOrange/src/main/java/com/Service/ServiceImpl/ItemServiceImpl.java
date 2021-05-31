package com.Service.ServiceImpl;

import com.Controller.ReturnType.CommonReturnType;
import com.Controller.ViewObject.ItemVo;
import com.Dao.DataObject.ItemAttributesDO;
import com.Dao.DataObject.ItemDO;
import com.Dao.ItemAttributesDOMapper;
import com.Dao.ItemDOMapper;
import com.Service.IService.BrandIService;
import com.Service.IService.ItemISerive;
import com.Service.IService.TypeIService;
import com.Service.Model.ItemAttributesModel;
import com.Service.Model.ItemModel;
import com.sun.tools.javac.jvm.Items;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ItemServiceImpl implements ItemISerive {

    @Autowired
    private ItemDOMapper itemDOMapper;
    @Autowired
    private ItemAttributesDOMapper itemAttributesDOMapper;
    @Autowired
    TypeIService typeServiceImpl;
    @Autowired
    BrandIService brandServiceImpl;

    @Override
    public int addItem(ItemModel itemModel) {
        ItemDO itemDO=new ItemDO(itemModel);
        itemDO.setSales(0);//初始化销量
        itemDOMapper.insert(itemDO);
        int itemid=itemDO.getItemid();
        ArrayList<ItemAttributesModel>  itemAttributesModels=itemModel.getItemAttributesList();
        for(int i=0;i<itemAttributesModels.size();i++){
            itemModel.getItemAttributesList().get(i).setItemid(itemDO.getItemid());
            ItemAttributesDO itemAttributesDO=new ItemAttributesDO(itemModel.getItemAttributesList().get(i));
            itemAttributesDOMapper.insert(itemAttributesDO);
        }
        return itemid;
    }

    @Override
    public int deleteItem(Integer itemid) {
        int result=itemDOMapper.deleteByPrimaryKey(itemid);
        return result;
    }

    @Override
    public ItemModel getItemById(Integer itemid) {
        ItemDO itemDO=itemDOMapper.selectByPrimaryKey(itemid);
        ItemModel itemModel=new ItemModel(itemDO);
        ArrayList<ItemAttributesDO> itemAttributesDOs=itemAttributesDOMapper.seletByItemID(itemid);
        ArrayList<ItemAttributesModel> itemAttributesModels=new ArrayList<>();
        for(int i=0;i<itemAttributesDOs.size();i++){
            itemAttributesModels.add(new ItemAttributesModel(itemAttributesDOs.get(i)));
        }
        itemModel.setItemAttributesList(itemAttributesModels);
        return itemModel;
    }

    @Override
    public int updateItem(ItemModel itemModel) {
        ItemDO itemDO=new ItemDO(itemModel);
        int result=itemDOMapper.updateByPrimaryKey(itemDO);
        if(result==0){
            return -1;
        }
        result=0;
        for(int i=0;i<itemModel.getItemAttributesList().size();i++){
            ItemAttributesDO itemAttributesDO=new ItemAttributesDO(itemModel.getItemAttributesList().get(i));
            result+=itemAttributesDOMapper.updateByPrimaryKey(itemAttributesDO);
        }
        if(result==itemModel.getItemAttributesList().size()){
            return result;
        }else{
            return 0;
        }
    }

    @Override
    public ArrayList<ItemModel> getItemList(String title, String brand, String type, int page, int limit) {
        if(StringUtils.isEmpty(title)||StringUtils.equals(title,"")){
            title=null;
        }else{
            title="%"+title+"%";
        }
        if(StringUtils.isEmpty(brand)||StringUtils.equals(brand,"")){
            brand=null;
        }else{
            brand="%"+brand+"%";
        }
        if(StringUtils.isEmpty(type)||StringUtils.equals(type,"")){
            type=null;
        }else{
            type="%"+type+"%";
        }
        ArrayList<ItemDO> itemDo=itemDOMapper.selectList(title,brand,type,page,page*(limit-1));
        ArrayList<ItemModel> models=new ArrayList<>();
        for(int i=0;i<itemDo.size();i++){
            models.add(new ItemModel(itemDo.get(i)));
        }
        return models;
    }

    @Override
    public ArrayList<ItemModel> getItemListByCondition(String condition, String condition_value, int page, int limit) {
        ArrayList<ItemDO> itemDo;
        //无条件查询
        if(StringUtils.isEmpty(condition)||StringUtils.equals("",condition)||StringUtils.isEmpty(condition_value)||StringUtils.equals("",condition_value)) {
            itemDo = itemDOMapper.select(page, page * (limit - 1));
        }
        //条件查询
        else{
            condition_value="%"+condition_value+"%";
            if(StringUtils.equals(condition,"商品名称")){
                itemDo=itemDOMapper.selectListByTitle(condition_value,page,page*(limit-1));
            }else if(StringUtils.equals(condition,"商品类别")){
                itemDo=itemDOMapper.selectListByType(condition_value,page,page*(limit-1));
            }else{
                itemDo=itemDOMapper.selectListByBrand(condition_value,page,page*(limit-1));
            }
        }
        ArrayList<ItemModel> models=new ArrayList<>();
        for(int i=0;i<itemDo.size();i++){
            models.add(new ItemModel(itemDo.get(i)));
        }
        return models;
    }

    @Override
    public ItemAttributesDO getItemAttributesByID(Integer itemattarbutesid) {
        return itemAttributesDOMapper.selectByPrimaryKey(itemattarbutesid);
    }

}
