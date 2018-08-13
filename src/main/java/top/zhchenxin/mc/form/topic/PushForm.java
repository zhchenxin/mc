package top.zhchenxin.mc.form.topic;

import java.util.Date;

public class PushForm {
    private Long messageId = 0L;
    private String topicName = "";
    private String message = "";
    private Integer delay = 0;

    public Integer getAvailableDate() {
        long currentTime = System.currentTimeMillis();
        currentTime += this.delay * 1000;
        return (int) (currentTime / 1000);
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }
}
