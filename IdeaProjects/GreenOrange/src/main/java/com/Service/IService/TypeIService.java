package com.Service.IService;

import com.Service.Model.ItemTypeModel;

import java.util.ArrayList;

public interface TypeIService {
    ArrayList<ItemTypeModel> getSonTypeByID(int id);

    ArrayList<ItemTypeModel> getType(int grade);

    int addType(ItemTypeModel itemTypeModel);
}
