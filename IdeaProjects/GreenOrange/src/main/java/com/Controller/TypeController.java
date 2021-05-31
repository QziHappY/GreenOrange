package com.Controller;

import com.Controller.ReturnType.CommonReturnType;
import com.Service.IService.TypeIService;
import com.Service.Model.ItemTypeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller("type")
@RequestMapping("/type")
@CrossOrigin
public class TypeController {
    @Autowired
    TypeIService typeServiceImpl;
    /**
     * 新增类别
     */
    @RequestMapping("/addType")
    @ResponseBody
    public CommonReturnType addType(@RequestBody ItemTypeModel itemTypeVo){
        int result=typeServiceImpl.addType(itemTypeVo);
        if(result>0){
            return CommonReturnType.create("");

        }
        return CommonReturnType.create("error","");
    }
    /**
     * 根据id查询其子分类
     */
    @RequestMapping("/getSonTypeByID")
    @ResponseBody
    public CommonReturnType getSonTypeByID(@RequestParam("id")int id){
        ArrayList<ItemTypeModel> list = typeServiceImpl.getSonTypeByID(id);
        return CommonReturnType.create(list);
    }

    /**
     * 根据等级查询grade级标题
     */
    @RequestMapping("/getType")
    @ResponseBody
    public CommonReturnType getType(@RequestParam("grade")int grade){
        ArrayList<ItemTypeModel> list = typeServiceImpl.getType(grade);
        return CommonReturnType.create(list);
    }
}
