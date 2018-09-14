package top.chenxin.mc.web.response.customer;

import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.web.response.AbstractResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateResponse extends AbstractResponse {
    private List<Topic> topicList;

    private Customer customer;

    public UpdateResponse(List<Topic> topicList, Customer customer) {
        this.topicList = topicList;
        this.customer = customer;
    }

    @Override
    protected Map getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("topicList", getTopic());
        map.put("customer", getCustomer());
        return map;
    }

    private List getTopic() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Topic topic : this.topicList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", topic.getId());
            map.put("name", topic.getName());
            list.add(map);
        }
        return list;
    }

    private Map getCustomer() {
        Map<String, Object> map = new HashMap<>();
        map.put("topicId", customer.getTopicId());
        map.put("name", customer.getName());
        map.put("api", customer.getApi());
        map.put("attempts", customer.getAttempts());
        map.put("timeout", customer.getTimeout());
        return map;
    }
}
