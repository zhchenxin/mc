package top.chenxin.mc.web.response.message;

import com.github.pagehelper.Page;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.entity.FailedMessage;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.web.response.PaginationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FailedResponse extends PaginationResponse {

    private Map<Long, Customer> customerMap;
    private Map<Long, Topic> topicMap;
    private Page<FailedMessage> messageList;

    public FailedResponse(Page<FailedMessage> messageList, List<Customer> customerList, List<Topic> topicList) {
        this.messageList = messageList;
        this.setPage(messageList);

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

    @Override
    protected List getList() {
        List<Map<String, Object>> list = new ArrayList<>();

        for (FailedMessage item : messageList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getId());
            map.put("message", item.getMessage());
            map.put("error", item.getError());
            map.put("attempts", item.getAttempts());
            map.put("createDate", Utils.simpleDate(item.getCreateDate()));
            map.put("topicName", getTopic(item).getName());
            map.put("customerName", getCustomer(item).getName());

            list.add(map);
        }

        return list;
    }

    private Topic getTopic(FailedMessage message) {
        return topicMap.get(message.getTopicId());
    }

    private Customer getCustomer(FailedMessage message) {
        return customerMap.get(message.getCustomerId());
    }
}
