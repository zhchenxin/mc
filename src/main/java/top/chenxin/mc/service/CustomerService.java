package top.chenxin.mc.service;

import top.chenxin.mc.request.customer.CreateForm;
import top.chenxin.mc.request.customer.ListForm;
import top.chenxin.mc.response.Response;

public interface CustomerService {
    void create(CreateForm form);

    Response search(ListForm listForm);
}
