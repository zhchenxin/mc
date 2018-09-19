package top.chenxin.mc.web.response.message;

import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.entity.Message;
import top.chenxin.mc.entity.MessageLog;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.web.response.AbstractResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailResponse extends AbstractResponse {

    private Message message;
    private Topic topic;
    private Customer customer;
    private List<MessageLog> logList;

    public DetailResponse(Message message, Topic topic, Customer customer, List<MessageLog> logList) {
        this.message = message;
        this.topic = topic;
        this.customer = customer;
        this.logList = logList;
    }

    @Override
    public Map getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", message.getId());
        map.put("message", message.getMessage());
        map.put("attempts", message.getAttempts());
        map.put("status", message.getStatus());
        map.put("available_date", message.getAvailableDate());
        map.put("create_date", message.getCreateDate());

        // 嵌套信息
        map.put("topic", getTopic());
        map.put("customer", getCustomer());
        map.put("logs", getLogs());
        return map;
    }

    private Map getTopic() {
        Map<String, Object> topic = new HashMap<>();
        topic.put("id", this.topic.getId());
        topic.put("name", this.topic.getName());
        topic.put("description", this.topic.getDescription());
        return topic;
    }

    private Map getCustomer() {
        Map<String, Object> customer = new HashMap<>();
        customer.put("id", this.customer.getId());
        customer.put("name", this.customer.getName());
        customer.put("api", this.customer.getApi());
        return customer;
    }

    private List getLogs() {
        List<Map<String, Object>> logs = new ArrayList<>();
        for (MessageLog log : this.logList) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", log.getId());
            item.put("response", log.getResponse());
            item.put("error", log.getError());
            item.put("time", log.getTime());
            item.put("create_date", log.getCreateDate());
            logs.add(item);
        }
        return logs;
    }
}
