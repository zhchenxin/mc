package top.chenxin.mc.service;

import top.chenxin.mc.form.customer.CreateForm;
import top.chenxin.mc.form.customer.ListForm;
import top.chenxin.mc.response.Response;
import top.chenxin.mc.response.customer.ListResponse;

public interface CustomerService {
    void create(CreateForm form);

    Response search(ListForm listForm);
}
