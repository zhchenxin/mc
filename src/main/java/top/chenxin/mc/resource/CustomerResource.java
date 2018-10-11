package top.chenxin.mc.resource;

import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.core.Resource;
import top.chenxin.mc.entity.Customer;

import java.util.HashMap;
import java.util.Map;

public class CustomerResource extends Resource {

    // topic id
    private Long topicId;

    private Long id;
    private String name;
    private String api;
    private Integer timeout;
    private Integer attempts;
    private Integer createDate;
    private Integer updateDate;

    public CustomerResource(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.api = customer.getApi();
        this.timeout = customer.getTimeout();
        this.attempts = customer.getAttempts();
        this.createDate = customer.getCreateDate();
        this.updateDate = customer.getUpdateDate();
        this.topicId = customer.getTopicId();
    }

    @Override
    protected Map<String, Object> getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("api", api);
        map.put("timeout", timeout);
        map.put("attempts", attempts);
        map.put("createDate", Utils.simpleDate(createDate));
        map.put("updateDate", Utils.simpleDate(updateDate));

        map.put("topicId", topicId);
        return map;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getApi() {
        return api;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public Integer getCreateDate() {
        return createDate;
    }

    public Integer getUpdateDate() {
        return updateDate;
    }

    public Long getTopicId() {
        return topicId;
    }
}
