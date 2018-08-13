package top.zhchenxin.mc.entity;

import top.zhchenxin.mc.lib.BaseEntity;

import java.util.Date;

public class Customer extends BaseEntity {
    // topic id
    private Long topicId;
    // 名称
    private String name;
    // 请求接口
    private String api;
    // 超时时间
    private Integer timeout;
    // 重试次数
    private Integer attempts;
    // 创建时间
    private Integer createDate;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Integer getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Integer createDate) {
        this.createDate = createDate;
    }
}
