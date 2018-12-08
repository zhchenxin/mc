package top.chenxin.mc.web.request.message_log;

import top.chenxin.mc.web.request.BaseListForm;

public class ListForm extends BaseListForm {
    private Long customerId;

    private Long messageId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
}
