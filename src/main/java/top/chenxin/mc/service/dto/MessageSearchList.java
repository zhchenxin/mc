package top.chenxin.mc.service.dto;

import com.github.pagehelper.Page;
import top.chenxin.mc.dao.po.Customer;
import top.chenxin.mc.dao.po.Message;
import top.chenxin.mc.dao.po.Topic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageSearchList {

    private Map<Long, Customer> customerMap;
    private Map<Long, Topic> topicMap;
    private Page<Message> messageList;

    public MessageSearchList(Page<Message> messageList, List<Customer> customerList, List<Topic> topicList) {
        this.messageList = messageList;

        this.customerMap = new HashMap<>();
        if (customerList != null) {
            for (Customer item : customerList) {
                this.customerMap.put(item.getId(), item);
            }
        }

        this.topicMap = new HashMap<>();
        if (topicList != null) {
            for (Topic item : topicList) {
                this.topicMap.put(item.getId(), item);
            }
        }
    }

    public Map<Long, Customer> getCustomerMap() {
        return customerMap;
    }

    public Map<Long, Topic> getTopicMap() {
        return topicMap;
    }

    public Page<Message> getMessageList() {
        return messageList;
    }
}
