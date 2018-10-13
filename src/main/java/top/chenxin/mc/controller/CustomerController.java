package top.chenxin.mc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.request.customer.ListForm;
import top.chenxin.mc.request.customer.CreateForm;
import top.chenxin.mc.request.customer.UpdateForm;
import top.chenxin.mc.service.CustomerService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("")
public class CustomerController extends BaseController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "customer", method = RequestMethod.GET)
    public Map index(@Validated ListForm form) {
        List<Long> ids = Utils.getIds(form.getId());
        return customerService.search(ids, form.getTopicId(), form.getPage(), form.getLimit()).toMap();
    }

    @RequestMapping(value = "customer", method = RequestMethod.POST)
    private Map create(@Validated CreateForm form) {
        customerService.insert(form.getTopicId(), form.getName(), form.getApi(), form.getTimeout(), form.getAttempts());
        return success();
    }

    @RequestMapping(value = "customer/{id}", method = RequestMethod.PUT)
    private Map update(@PathVariable("id") Long id, @Validated UpdateForm form) {
        customerService.update(id, form.getName(), form.getApi(), form.getTimeout(), form.getAttempts());
        return success();
    }

    @RequestMapping(value = "customer/{id}", method = RequestMethod.DELETE)
    private Map delete(@PathVariable("id") Long id) {
        customerService.delete(id);
        return success();
    }

}
