package top.chenxin.mc.request.customer;

import top.chenxin.mc.request.BaseListForm;

public class ListForm extends BaseListForm {
    private Long topicId = 0L;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }
}
