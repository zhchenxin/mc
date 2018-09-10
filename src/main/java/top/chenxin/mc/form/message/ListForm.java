package top.chenxin.mc.form.message;

import top.chenxin.mc.lib.BaseListForm;

public class ListForm extends BaseListForm {
    private Long topicId = 0L;
    private Long customerId = 0L;
    private Integer status = 0;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
