package com.Service.IService;

import com.Controller.ViewObject.ItemVo;
import com.Dao.DataObject.ItemAttributesDO;
import com.Service.Model.ItemModel;
import org.springframework.data.redis.connection.stream.StreamInfo;

import java.util.ArrayList;

public interface ItemISerive {
    int addItem(ItemModel itemModel);

    int deleteItem(Integer itemid);

    ItemModel getItemById(Integer itemid);

    int updateItem(ItemModel itemModel);

    ArrayList<ItemModel> getItemList(String title, String brand, String type, int page, int limit);

    ArrayList<ItemModel> getItemListByCondition(String condition, String condition_value, int page, int limit);

    ItemAttributesDO getItemAttributesByID(Integer itemattarbutesid);

    int updateStock(int itemAttributesid, int amount, String type);

}
