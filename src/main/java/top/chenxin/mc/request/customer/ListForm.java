package top.chenxin.mc.request.customer;

import top.chenxin.mc.request.BaseListForm;

public class ListForm extends BaseListForm {
    private Long topicId = 0L;

    private String id;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
