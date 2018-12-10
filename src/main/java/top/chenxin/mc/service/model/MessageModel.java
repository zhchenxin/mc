package top.chenxin.mc.service.model;

import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.entity.FailedMessage;
import top.chenxin.mc.entity.Message;

public class MessageModel {

    private Long id;

    private Long customerId;

    private Long topicId;

    private String api;

    // 最大重试次数
    private Integer maxAttempts;

    // 当前重试次数
    private Integer attempts;

    // 消息内容
    private String message;

    // 超时时间
    private Integer timeout;

    private Integer delay;

    // 是否记录日志
    private Boolean isLog;
    // 优先级:0=低,50=正常,100=高
    private Integer priority;

    // redis 中的 reserved 文本
    private String reserved;

    public MessageModel() {

    }

    public MessageModel(Customer customer) {
        initWithCustomer(customer);
    }

    public MessageModel(Message message, Customer customer) {
        setId(message.getId());
        setMessage(message.getMessage());
        initWithCustomer(customer);
    }

    public MessageModel(FailedMessage message, Customer customer, int delay) {
        setId(message.getId());
        setAttempts(message.getAttempts());
        setMessage(message.getMessage());
        setDelay(delay);
        initWithCustomer(customer);
    }

    private void initWithCustomer(Customer customer) {
        setTopicId(customer.getTopicId());
        setCustomerId(customer.getId());
        setMaxAttempts(customer.getAttempts());
        setApi(customer.getApi());
        setTimeout(customer.getTimeout());
        setIsLog(customer.getIsLog());
        setPriority(customer.getPriority());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
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
