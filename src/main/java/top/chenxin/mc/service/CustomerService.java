package top.chenxin.mc.service;

import top.chenxin.mc.form.customer.CreateForm;
import top.chenxin.mc.form.customer.ListForm;
import top.chenxin.mc.resource.CustomerCollection;

public interface CustomerService {
    void create(CreateForm form);

    CustomerCollection search(ListForm listForm);
}
