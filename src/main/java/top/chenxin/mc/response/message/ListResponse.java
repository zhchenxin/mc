package top.chenxin.mc.response.message;

import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.entity.Message;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.lib.Utils;
import top.chenxin.mc.response.PaginationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListResponse extends PaginationResponse {
    private Map<Long, Customer> customerMap;

    private Map<Long, Topic> topicMap;

    private List<Message> messageList;

    public ListResponse(List<Message> messageLis, List<Customer> customerList, List<Topic> topicList) {
        this.messageList = messageLis;

        this.customerMap = new HashMap<>();
        for (Customer item : customerList) {
            this.customerMap.put(item.getId(), item);
        }

        this.topicMap = new HashMap<>();
        for (Topic item : topicList) {
            this.topicMap.put(item.getId(), item);
        }
    }

    @Override
    protected List getList() {
        List<Map<String, Object>> list = new ArrayList<>();

        for (Message item : messageList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getId());
            map.put("message", item.getMessage());
            map.put("attempts", item.getAttempts());
            map.put("status", item.getStatus());
            map.put("availableDate", Utils.simpleDate(item.getAvailableDate()));
            map.put("createDate", Utils.simpleDate(item.getCreateDate()));
            map.put("topicName", topicMap.get(item.getTopicId()).getName());
            map.put("customerName", customerMap.get(item.getCustomerId()).getName());

            list.add(map);
        }

        return list;
    }
}
