package top.chenxin.mc.web.response.message;

import top.chenxin.mc.dao.po.Customer;
import top.chenxin.mc.dao.po.Message;
import top.chenxin.mc.dao.po.Topic;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.service.dto.MessageSearchList;
import top.chenxin.mc.web.response.PaginationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListResponse extends PaginationResponse {
    private MessageSearchList messageSearchList;

    public ListResponse(MessageSearchList messageSearchList) {
        this.messageSearchList = messageSearchList;
        this.setPage(messageSearchList.getMessageList());
    }

    @Override
    protected List getList() {
        List<Map<String, Object>> list = new ArrayList<>();

        for (Message item : this.messageSearchList.getMessageList()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getId());
            map.put("message", item.getMessage());
            map.put("attempts", item.getAttempts());
            map.put("status", item.getStatus());
            map.put("availableDate", Utils.simpleDate(item.getAvailableDate()));
            map.put("createDate", Utils.simpleDate(item.getCreateDate()));
            map.put("topicName", getTopic(item).getName());
            map.put("customerName", getCustomer(item).getName());

            list.add(map);
        }

        return list;
    }

    private Topic getTopic(Message message) {
        return this.messageSearchList.getTopicMap().get(message.getTopicId());
    }

    private Customer getCustomer(Message message) {
        return this.messageSearchList.getCustomerMap().get(message.getCustomerId());
    }
}
