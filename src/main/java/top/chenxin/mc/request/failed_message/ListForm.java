package top.chenxin.mc.request.failed_message;

import top.chenxin.mc.request.BaseListForm;

public class ListForm extends BaseListForm {
    private Long customerId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
