package top.chenxin.mc.web.response.message;

import com.github.pagehelper.Page;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.entity.Message;
import top.chenxin.mc.entity.MessageLog;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.web.response.PaginationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogResponse extends PaginationResponse {

    private Map<Long, Customer> customerMap;
    private Map<Long, Topic> topicMap;
    private Page<MessageLog> messageLogPage;

    public LogResponse(Page<MessageLog> messageLogPage, List<Customer> customerList, List<Topic> topicList) {
        this.messageLogPage = messageLogPage;
        this.setPage(messageLogPage);

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

        for (MessageLog item : messageLogPage) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getId());
            map.put("request", item.getRequest());
            map.put("response", item.getResponse());
            map.put("error", item.getError());
            map.put("createDate", Utils.simpleDate(item.getCreateDate()));
            map.put("topicName", getTopic(item).getName());
            map.put("customerName", getCustomer(item).getName());

            list.add(map);
        }

        return list;
    }

    private Topic getTopic(MessageLog message) {
        return topicMap.get(message.getTopicId());
    }

    private Customer getCustomer(MessageLog message) {
        return customerMap.get(message.getCustomerId());
    }
}
