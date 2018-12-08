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
    private int maxAttempts;

    // 当前重试次数
    private int attempts;

    // 消息内容
    private String message;

    // 超时时间
    private int timeout;

    private int delay;

    private String reserved;

    public MessageModel() {

    }

    public MessageModel(Message message, Customer customer) {
        setId(message.getId());
        setTopicId(message.getTopicId());
        setCustomerId(message.getCustomerId());
        setAttempts(message.getAttempts());
        setMaxAttempts(customer.getAttempts());
        setApi(customer.getApi());
        setMessage(message.getMessage());
        setTimeout(customer.getTimeout());
    }

    public MessageModel(FailedMessage message, Customer customer, int delay) {
        setId(message.getId());
        setTopicId(message.getTopicId());
        setCustomerId(message.getCustomerId());
        setAttempts(message.getAttempts());
        setMaxAttempts(customer.getAttempts());
        setApi(customer.getApi());
        setMessage(message.getMessage());
        setTimeout(customer.getTimeout());
        setDelay(delay);
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
}
