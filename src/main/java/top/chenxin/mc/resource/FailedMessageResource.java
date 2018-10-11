package top.chenxin.mc.resource;

import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.core.Resource;
import top.chenxin.mc.entity.FailedMessage;

import java.util.HashMap;
import java.util.Map;

public class FailedMessageResource extends Resource {

    private Long topicId;
    private Long customerId;

    private Long id;
    private String message;
    private String error;
    private Integer attempts;
    private Integer createDate;

    public FailedMessageResource(FailedMessage failedMessage) {
        this.id = failedMessage.getId();
        this.message = failedMessage.getMessage();
        this.error = failedMessage.getError();
        this.attempts = failedMessage.getAttempts();
        this.createDate = failedMessage.getCreateDate();
        this.topicId = failedMessage.getTopicId();
        this.customerId = failedMessage.getCustomerId();
    }

    @Override
    protected Map<String, Object> getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("message", message);
        map.put("error", error);
        map.put("attempts", attempts);
        map.put("createDate", Utils.simpleDate(createDate));
        return map;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public Integer getCreateDate() {
        return createDate;
    }

    public Long getTopicId() {
        return topicId;
    }

    public Long getCustomerId() {
        return customerId;
    }
}
