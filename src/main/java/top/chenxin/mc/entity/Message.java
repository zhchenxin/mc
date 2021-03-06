package top.chenxin.mc.entity;

import top.chenxin.mc.core.BaseEntity;

public class Message extends BaseEntity {

    public static final int StatusWatting = 1; // 等待中
    public static final int StatusRunning = 2; // 执行中

    private Long topicId;
    private Long customerId;
    // 消息内容
    private String message;
    // 重试次数
    private Integer attempts;
    // 状态
    private Integer status;
    // 延迟执行时间
    private Integer availableDate;
    // 创建时间
    private Integer createDate;
    // 超时时间
    private Integer timeoutDate;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(Integer availableDate) {
        this.availableDate = availableDate;
    }

    public Integer getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Integer createDate) {
        this.createDate = createDate;
    }

    public Integer getTimeoutDate() {
        return timeoutDate;
    }

    public void setTimeoutDate(Integer timeoutDate) {
        this.timeoutDate = timeoutDate;
    }
}
