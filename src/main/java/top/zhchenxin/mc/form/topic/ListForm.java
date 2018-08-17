package top.zhchenxin.mc.form.topic;

import top.zhchenxin.mc.lib.BaseListForm;

public class ListForm extends BaseListForm {
    private String name = "";
    private Long topicId = 0L;

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
