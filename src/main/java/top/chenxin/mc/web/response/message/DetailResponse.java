package top.chenxin.mc.web.response.message;

import top.chenxin.mc.dao.po.MessageLog;
import top.chenxin.mc.service.dto.MessageDetail;
import top.chenxin.mc.web.response.AbstractResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailResponse extends AbstractResponse {

    private MessageDetail messageDetail;

    public DetailResponse(MessageDetail messageDetail) {
        this.messageDetail = messageDetail;
    }

    @Override
    public Map getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", messageDetail.getMessage().getId());
        map.put("message_id", messageDetail.getMessage().getMessageId());
        map.put("message", messageDetail.getMessage().getMessage());
        map.put("attempts", messageDetail.getMessage().getAttempts());
        map.put("status", messageDetail.getMessage().getStatus());
        map.put("available_date", messageDetail.getMessage().getAvailableDate());
        map.put("create_date", messageDetail.getMessage().getCreateDate());

        // topic
        Map<String, Object> topic = new HashMap<>();
        topic.put("id", messageDetail.getTopic().getId());
        topic.put("name", messageDetail.getTopic().getName());
        topic.put("description", messageDetail.getTopic().getDescription());
        map.put("topic", topic);

        // customer
        Map<String, Object> customer = new HashMap<>();
        customer.put("id", messageDetail.getCustomer().getId());
        customer.put("name", messageDetail.getCustomer().getName());
        customer.put("api", messageDetail.getCustomer().getApi());
        map.put("customer", customer);

        // logs
        List<Map<String, Object>> logs = new ArrayList<>();
        for (MessageLog log : messageDetail.getLogList()) {
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
