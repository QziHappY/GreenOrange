package com.Service.ServiceImpl;

import com.Dao.DataObject.ItemTypeDO;
import com.Dao.ItemTypeDOMapper;
import com.Service.IService.TypeIService;
import com.Service.Model.ItemTypeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TypeServiceImpl implements TypeIService {
    @Autowired
    ItemTypeDOMapper itemTypeDOMapper;
    @Override
    public ArrayList<ItemTypeModel> getSonTypeByID(int id) {
        ArrayList<ItemTypeDO> list = itemTypeDOMapper.getSonTypeByID(id);
        ArrayList<ItemTypeModel> arrayList=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            arrayList.add(new ItemTypeModel(list.get(i)));
        }
        return arrayList;
    }

    @Override
    public ArrayList<ItemTypeModel> getType(int grade) {
        ArrayList<ItemTypeDO> list = itemTypeDOMapper.getType(grade);
        ArrayList<ItemTypeModel> arrayList=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            arrayList.add(new ItemTypeModel(list.get(i)));
        }
        return arrayList;
    }

    @Override
    public int addType(ItemTypeModel itemTypeModel) {
        ItemTypeDO itemTypeDO=new ItemTypeDO(itemTypeModel);
        int result=itemTypeDOMapper.insert(itemTypeDO);
        return result;
    }
}
