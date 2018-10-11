package top.chenxin.mc.entity;

import top.chenxin.mc.core.BaseEntity;

public class Cron extends BaseEntity {

    public static final int StatusNormal = 1; // 正常状态
    public static final int StatusStop = 2;   // 暂停状态

    // 名称
    private String name;

    // topic id
    private Long topicId;

    // 描述
    private String description;

    // cron 描述符(从秒开始)
    private String spec;

    // 创建时间
    private Integer createDate;

    // 更新时间
    private Integer updateDate;

    // 删除时间
    private Integer deleteDate;

    // 状态
    private Integer status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Integer getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Integer createDate) {
        this.createDate = createDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Integer updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Integer deleteDate) {
        this.deleteDate = deleteDate;
    }
}
