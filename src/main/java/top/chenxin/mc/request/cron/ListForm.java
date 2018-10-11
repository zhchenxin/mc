package top.chenxin.mc.request.cron;

import top.chenxin.mc.request.BaseListForm;

public class ListForm extends BaseListForm {
    private Long topicId;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }
}
