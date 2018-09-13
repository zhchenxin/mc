package top.chenxin.mc.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.chenxin.mc.service.dto.CustomerSearchList;
import top.chenxin.mc.web.request.customer.ListForm;
import top.chenxin.mc.web.request.customer.CreateForm;
import top.chenxin.mc.web.response.SuccessResponse;
import top.chenxin.mc.service.CustomerService;
import top.chenxin.mc.web.response.customer.ListResponse;

import java.util.Map;

@RestController
@RequestMapping("customer")
public class CustomerController extends BaseController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map list(@Validated ListForm form) {
        CustomerSearchList customerSearchList = customerService.search(form.getTopicId(), form.getPage(), form.getLimit());
        return new ListResponse(customerSearchList).toMap();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    private Map create(@Validated CreateForm form) {
        customerService.insert(form.getTopicId(), form.getName(), form.getApi(), form.getTimeout(), form.getAttempts());
        return new SuccessResponse().toMap();
    }

}
