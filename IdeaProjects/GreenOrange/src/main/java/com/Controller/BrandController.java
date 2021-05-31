package com.Controller;

import com.Controller.ReturnType.CommonReturnType;
import com.Controller.ViewObject.BrandVo;
import com.Service.IService.BrandIService;
import com.Service.Model.BrandModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@Controller("brand")
@RequestMapping("/brand")
@CrossOrigin
public class BrandController {

    @Autowired
    BrandIService brandServiceImpl;
    /**
     * 分页列表查看品牌
     */
    @RequestMapping("/getBrandList")
    @ResponseBody
    public CommonReturnType getBrandList(@RequestParam("limit")int limit,@RequestParam("page")int page){
        //第page页，每页limit个记录
        ArrayList<BrandModel> listModel=brandServiceImpl.selectBrand(limit,page);
        ArrayList<BrandVo> list=new ArrayList<>();
        for(int i=0;i<listModel.size();i++){
            list.add(new BrandVo(listModel.get(i)));
        }
        int allpage=brandServiceImpl.getAllpage(page);
        int allcount=brandServiceImpl.getAllcount();
        HashMap<String,Object> map = new HashMap<>();
        map.put("list",list);
        map.put("allpage",allpage);
        map.put("allcount",allcount);
        return CommonReturnType.create(map);
    }

    /**
     * 查看品牌全部信息
     */
    @RequestMapping("/getBrands")
    @ResponseBody
    public CommonReturnType getBrands(){
        ArrayList<BrandModel> listModel=brandServiceImpl.selectAll();
        if(listModel==null||listModel.size()==0){
            return CommonReturnType.create(new ArrayList<BrandVo>());
        }
        ArrayList<BrandVo> list=new ArrayList<>();
        for(int i=0;i<listModel.size();i++){
            list.add(new BrandVo(listModel.get(i)));
        }
        return CommonReturnType.create(list);
    }
    /**
     * 添加新品牌
     */
    @RequestMapping("/addBrand")
    @ResponseBody
    public CommonReturnType addBrand(@RequestBody BrandVo brand){
        int result=brandServiceImpl.addBrand(brand);
        if(result>0){
            return CommonReturnType.create("");
        }
        return CommonReturnType.create("error","");
    }
    /**
     * 查看品牌信息
     */

    /**
     * 修改品牌信息
     */

    /**
     * 删除品牌
     */
}
