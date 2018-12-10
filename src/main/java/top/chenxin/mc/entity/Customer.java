package top.chenxin.mc.entity;

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
    // 是否记录日志
    private Boolean isLog;
    // 优先级:0=低,50=正常,100=高
    private Integer priority;
    // 创建时间
    private Integer createDate;

    private Integer updateDate;

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

    public Integer getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Integer updateDate) {
        this.updateDate = updateDate;
    }

    public Boolean getIsLog() {
        return isLog;
    }

    public void setIsLog(Boolean log) {
        isLog = log;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
