package com.Service.ServiceImpl;

import com.Controller.ViewObject.BrandVo;
import com.Dao.BrandDOMapper;
import com.Dao.DataObject.BrandDO;
import com.Service.IService.BrandIService;
import com.Service.Model.BrandModel;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BrandServiceImpl implements BrandIService {
    @Autowired
    BrandDOMapper brandDOMapper;
    @Override
    public ArrayList<BrandModel> selectBrand(int limit, int page) {
        ArrayList<BrandDO> brandDOS=brandDOMapper.selectLimitPage((limit-1)*page,page);
        if(brandDOS.size()==0){
            return null;
        }
        ArrayList<BrandModel>  list=new ArrayList<BrandModel>();
        for(int i=0;i<brandDOS.size();i++){
            list.add(new BrandModel(brandDOS.get(i)));
        }
        return list;
    }

    @Override
    public ArrayList<BrandModel> selectAll() {
        ArrayList<BrandDO> brandDOS=brandDOMapper.selectAll();
        if(brandDOS.size()==0){
            return null;
        }
        ArrayList<BrandModel>  list=new ArrayList<BrandModel>();
        for(int i=0;i<brandDOS.size();i++){
            list.add(new BrandModel(brandDOS.get(i)));
        }
        return list;
    }

    @Override
    public int addBrand(BrandVo brand) {
        BrandDO brandDO=new BrandDO(brand);
        int result=brandDOMapper.insert(brandDO);
        return result;
    }

    @Override
    public int getAllpage(int page) {
        int allcount=getAllcount();
        if(allcount%page==0){
            return allcount/page;
        }else{
            return allcount/page+1;
        }
    }

    @Override
    public int getAllcount() {
        return brandDOMapper.selectAll().size();
    }
}
