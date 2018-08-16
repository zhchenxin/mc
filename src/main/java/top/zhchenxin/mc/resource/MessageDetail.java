package top.zhchenxin.mc.resource;

import top.zhchenxin.mc.entity.Customer;
import top.zhchenxin.mc.entity.MessageLog;
import top.zhchenxin.mc.entity.Topic;
import top.zhchenxin.mc.lib.DetailResource;
import top.zhchenxin.mc.entity.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageDetail extends DetailResource<Message> {

    private Topic topic;
    private Customer customer;
    private List<MessageLog> logList;

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.getEntity().getId());
        map.put("message_id", this.getEntity().getMessageId());
        map.put("attempts", this.getEntity().getAttempts());
        map.put("status", this.getEntity().getStatus());
        map.put("available_date", this.getEntity().getAvailableDate());
        map.put("create_date", this.getEntity().getCreateDate());

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
        return super.toMap();
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<MessageLog> getLogList() {
        return logList;
    }

    public void setLogList(List<MessageLog> logList) {
        this.logList = logList;
    }
}
