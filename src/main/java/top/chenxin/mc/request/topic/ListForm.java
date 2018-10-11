package top.chenxin.mc.request.topic;

import top.chenxin.mc.request.BaseListForm;

public class ListForm extends BaseListForm {
    private String name;

    private Long topicId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }
}
