package top.chenxin.mc.form.customer;

import top.chenxin.mc.entity.Customer;

import javax.validation.constraints.NotNull;

public class CreateForm {

    @NotNull
    private Long topicId = 0L;
    @NotNull
    private String name = "";
    @NotNull
    private String api = "";
    private Integer timeout = 60;
    private Integer attempts = 3;

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

    public Customer toCustomer() {
        Customer item = new Customer();
        item.setTopicId(this.topicId);
        item.setName(this.name);
        item.setApi(this.api);
        item.setAttempts(this.attempts);
        item.setTimeout(this.timeout);
        return item;
    }
}
