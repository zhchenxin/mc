package top.chenxin.mc.resource;

import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.core.Resource;
import top.chenxin.mc.entity.MessageLog;

import java.util.HashMap;
import java.util.Map;

public class MessageLogResource extends Resource {

    private Long topicId;
    private Long customerId;

    private Long id;
    private String request;
    private String response;
    private String error;
    private Integer time;
    private Integer createDate;

    public MessageLogResource(MessageLog messageLog) {
        this.topicId = messageLog.getTopicId();
        this.customerId = messageLog.getCustomerId();
        this.id = messageLog.getId();
        this.request = messageLog.getRequest();
        this.response = messageLog.getResponse();
        this.error = messageLog.getError();
        this.time = messageLog.getTime();
        this.createDate = messageLog.getCreateDate();
    }

    @Override
    protected Map<String, Object> getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("request", request);
        map.put("response", response);
        map.put("error", error);
        map.put("time", time);
        map.put("createDate", Utils.simpleDate(createDate));

        map.put("topicId", topicId);
        map.put("customerId", customerId);
        return map;
    }

    public Long getTopicId() {
        return topicId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getId() {
        return id;
    }

    public String getRequest() {
        return request;
    }

    public String getResponse() {
        return response;
    }

    public String getError() {
        return error;
    }

    public Integer getTime() {
        return time;
    }

    public Integer getCreateDate() {
        return createDate;
    }
}
