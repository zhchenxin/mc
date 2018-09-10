package top.chenxin.mc.response.message;

import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.entity.Message;
import top.chenxin.mc.entity.MessageLog;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.response.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailResponse implements Response {

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
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", message.getId());
        map.put("message_id", message.getMessageId());
        map.put("message", message.getMessage());
        map.put("attempts", message.getAttempts());
        map.put("status", message.getStatus());
        map.put("available_date", message.getAvailableDate());
        map.put("create_date", message.getCreateDate());

        // topic
        Map<String, Object> topic = new HashMap<>();
        topic.put("id", this.topic.getId());
        topic.put("name", this.topic.getName());
        topic.put("description", this.topic.getDescription());
        map.put("topic", topic);

        // customer
        Map<String, Object> customer = new HashMap<>();
        customer.put("id", this.customer.getId());
        customer.put("name", this.customer.getName());
        customer.put("api", this.customer.getApi());
        map.put("customer", customer);

        // logs
        List<Map<String, Object>> logs = new ArrayList<>();
        for (MessageLog log : logList) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", log.getId());
            item.put("response", log.getResponse());
            item.put("error", log.getError());
            item.put("time", log.getTime());
            item.put("create_date", log.getCreateDate());
            logs.add(item);
        }
        map.put("logs", logs);
        return map;
    }
}
