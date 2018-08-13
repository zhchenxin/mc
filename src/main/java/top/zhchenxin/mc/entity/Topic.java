package top.zhchenxin.mc.entity;


import top.zhchenxin.mc.lib.BaseEntity;

import java.util.Date;

public class Topic extends BaseEntity {
    private String name;
    private String description;
    private Integer createDate;

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

    public Integer getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Integer createDate) {
        this.createDate = createDate;
    }
}
