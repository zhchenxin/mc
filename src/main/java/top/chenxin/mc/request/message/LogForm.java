package top.chenxin.mc.request.message;

import top.chenxin.mc.request.BaseListForm;

public class LogForm extends BaseListForm {
    private Long customerId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
