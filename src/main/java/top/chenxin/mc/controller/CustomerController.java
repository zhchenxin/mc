package top.chenxin.mc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.chenxin.mc.request.customer.ListForm;
import top.chenxin.mc.request.customer.CreateForm;
import top.chenxin.mc.lib.BaseController;
import top.chenxin.mc.response.SuccessResponse;
import top.chenxin.mc.service.CustomerService;
import java.util.Map;

@RestController
@RequestMapping("customer")
public class CustomerController extends BaseController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map list(@Validated ListForm form) {
        return customerService.search(form).toMap();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    private Map create(@Validated CreateForm form) {
        customerService.create(form);
        return new SuccessResponse().toMap();
    }

}
