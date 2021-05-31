package com.Service.IService;

import com.Controller.ViewObject.BrandVo;
import com.Service.Model.BrandModel;

import java.util.ArrayList;

public interface BrandIService {
    ArrayList<BrandModel> selectBrand(int limit, int page);
    ArrayList<BrandModel> selectAll();
    int addBrand(BrandVo brand);
    //根据每页page个，分页，返回共页数
    int getAllpage(int page);
    //返回总条数
    int getAllcount();
}