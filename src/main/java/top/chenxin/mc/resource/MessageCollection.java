package top.chenxin.mc.resource;

import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.entity.Message;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.lib.PaginationCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageCollection extends PaginationCollection<Message> {
    private Map<Long, Customer> customerMap;

    private Map<Long, Topic> topicMap;

    @Override
    protected List formatList() {
        List<Map<String, Object>> list = new ArrayList<>();

        for (int i = 0 ; i < getList().size(); i++) {
            Message item = getList().get(i);

            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getId());
            map.put("messageId", item.getMessageId());
            map.put("message", item.getMessage());
            map.put("attempts", item.getAttempts());
            map.put("status", item.getStatus());
            map.put("availableDate", item.getAvailableDate());
            map.put("createDate", item.getCreateDate());
            map.put("topicId", item.getTopicId());
            map.put("customerId", item.getCustomerId());
            map.put("topicName", topicMap.get(item.getTopicId()).getName());
            map.put("customerName", customerMap.get(item.getCustomerId()).getName());

            list.add(map);
        }

        return list;
    }

    public void setCustomerList(List<Customer> customerList) {
        customerMap = new HashMap<>();

        for (Customer item : customerList) {
            customerMap.put(item.getId(), item);
        }
    }

    public void setTopicList(List<Topic> topicList) {
        topicMap = new HashMap<>();

        for (Topic item : topicList) {
            topicMap.put(item.getId(), item);
        }
    }
}
