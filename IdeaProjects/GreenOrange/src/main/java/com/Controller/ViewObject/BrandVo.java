package com.Controller.ViewObject;

import com.Service.Model.BrandModel;

public class BrandVo {
    private Integer brandid;
    private String name;
    private String description;

    public BrandVo() {
    }
    public BrandVo( BrandModel model) {
        this.brandid=model.getBrandid();
        this.name=model.getName();
        this.description=model.getDescription();
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
