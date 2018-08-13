package top.zhchenxin.mc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.zhchenxin.mc.form.customer.ListForm;
import top.zhchenxin.mc.form.customer.CreateForm;
import top.zhchenxin.mc.lib.BaseController;
import top.zhchenxin.mc.lib.PaginationCollection;
import top.zhchenxin.mc.service.CustomerService;
import java.util.Map;

@RestController
@RequestMapping("customer")
public class CustomerController extends BaseController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> list(ListForm form) {
        PaginationCollection collection = this.customerService.search(form);
        return this.successJson(collection.output());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    private Map<String, Object> create(CreateForm form) {
        this.customerService.create(form);
        return this.successJson(null);
    }

}
