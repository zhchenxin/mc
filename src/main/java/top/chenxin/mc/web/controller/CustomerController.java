package top.chenxin.mc.web.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.web.APIResponse;
import top.chenxin.mc.web.request.customer.ListForm;
import top.chenxin.mc.web.request.customer.CreateForm;
import top.chenxin.mc.web.request.customer.UpdateForm;
import top.chenxin.mc.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("")
public class CustomerController extends BaseController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "customer", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> index(@Validated ListForm form) {
        List<Long> ids = Utils.getIds(form.getId());
        return APIResponse.success(customerService.getPage(ids, form.getTopicId(), form.getPage(), form.getLimit()));
    }

    @RequestMapping(value = "customer", method = RequestMethod.POST)
    private ResponseEntity<JSONObject> create(@Validated CreateForm form) {
        customerService.insert(form.getTopicId(), form.getName(), form.getApi(), form.getTimeout(), form.getAttempts(), form.isLog(), form.getPriority());
        return APIResponse.success();
    }

    @RequestMapping(value = "customer/{id}", method = RequestMethod.PUT)
    private ResponseEntity<JSONObject> update(@PathVariable("id") Long id, @Validated UpdateForm form) {
        customerService.update(id, form.getName(), form.getApi(), form.getTimeout(), form.getAttempts(), form.isLog(), form.getPriority());
        return APIResponse.success();
    }

    @RequestMapping(value = "customer/{id}", method = RequestMethod.DELETE)
    private ResponseEntity<JSONObject> delete(@PathVariable("id") Long id) {
        customerService.delete(id);
        return APIResponse.success();
    }

}
