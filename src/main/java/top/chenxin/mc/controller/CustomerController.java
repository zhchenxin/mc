package top.chenxin.mc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.chenxin.mc.form.customer.ListForm;
import top.chenxin.mc.form.customer.CreateForm;
import top.chenxin.mc.lib.BaseController;
import top.chenxin.mc.lib.PaginationCollection;
import top.chenxin.mc.service.CustomerService;
import java.util.Map;

@RestController
@RequestMapping("customer")
public class CustomerController extends BaseController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> list(ListForm form) {
        PaginationCollection collection = customerService.search(form);
        return successJson(collection.toMap());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    private Map<String, Object> create(CreateForm form) {
        customerService.create(form);
        return successJson(null);
    }

}
