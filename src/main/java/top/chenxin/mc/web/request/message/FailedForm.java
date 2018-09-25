package top.chenxin.mc.web.request.message;

import top.chenxin.mc.web.request.BaseListForm;

public class FailedForm extends BaseListForm {
    private Long customerId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
