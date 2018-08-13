package top.zhchenxin.mc.service;

import top.zhchenxin.mc.entity.Customer;
import top.zhchenxin.mc.form.customer.CreateForm;
import top.zhchenxin.mc.form.customer.ListForm;
import top.zhchenxin.mc.resource.CustomerCollection;

public interface CustomerService {
    void create(CreateForm form);

    CustomerCollection search(ListForm listForm);

    Customer getById(Long id);
}
