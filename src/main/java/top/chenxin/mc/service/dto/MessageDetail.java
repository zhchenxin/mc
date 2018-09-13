package top.chenxin.mc.service.dto;

import top.chenxin.mc.dao.po.Customer;
import top.chenxin.mc.dao.po.Message;
import top.chenxin.mc.dao.po.MessageLog;
import top.chenxin.mc.dao.po.Topic;

import java.util.List;

public class MessageDetail {
    private Message message;
    private Topic topic;
    private Customer customer;
    private List<MessageLog> logList;

    public MessageDetail(Message message, Topic topic, Customer customer, List<MessageLog> logList) {
        this.message = message;
        this.topic = topic;
        this.customer = customer;
        this.logList = logList;
    }

    public Message getMessage() {
        return message;
    }

    public Topic getTopic() {
        return topic;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<MessageLog> getLogList() {
        return logList;
    }
}
