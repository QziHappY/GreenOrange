package com.Service.Model;

import com.Dao.DataObject.ItemTypeDO;

public class ItemTypeModel {
    private Integer typeid;
    private String title;
    private Integer level;
    private Integer fatherid;

    public ItemTypeModel() {
    }
    public ItemTypeModel(ItemTypeDO itemTypeDO) {
        this.typeid=itemTypeDO.getTypeid();
        this.title=itemTypeDO.getTitle();
        this.level=itemTypeDO.getLevel();
        this.fatherid=itemTypeDO.getFatherid();
    }

    public Integer getTypeid() {
        return typeid;
    }

    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getFatherid() {
        return fatherid;
    }

    public void setFatherid(Integer fatherid) {
        this.fatherid = fatherid;
    }
}
