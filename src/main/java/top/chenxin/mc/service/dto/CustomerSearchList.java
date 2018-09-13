package top.chenxin.mc.service.dto;

import com.github.pagehelper.Page;
import top.chenxin.mc.dao.po.Customer;
import top.chenxin.mc.dao.po.Topic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerSearchList {

    private Map<Long, Topic> topicMap;
    private Page<Customer> customerList;

    public CustomerSearchList(Page<Customer> customerList, List<Topic> topicList) {
        this.customerList = customerList;
        this.topicMap = new HashMap<>();

        if (topicList != null ) {
            for (Topic item : topicList) {
                this.topicMap.put(item.getId(), item);
            }
        }
    }

    public Map<Long, Topic> getTopicMap() {
        return topicMap;
    }

    public Page<Customer> getCustomerList() {
        return customerList;
    }
}
