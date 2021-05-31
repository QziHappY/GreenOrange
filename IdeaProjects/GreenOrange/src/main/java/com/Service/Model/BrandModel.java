package com.Service.Model;

import com.Dao.DataObject.BrandDO;

public class BrandModel {
    private Integer brandid;
    private String name;
    private String description;

    public BrandModel() {
    }
    public BrandModel(BrandDO brandDO) {
        this.brandid=brandDO.getBrandid();
        this.name=brandDO.getName();
        this.description=brandDO.getDescription();
    }

    public Integer getBrandid() {
        return brandid;
    }

    public void setBrandid(Integer brandid) {
        this.brandid = brandid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
