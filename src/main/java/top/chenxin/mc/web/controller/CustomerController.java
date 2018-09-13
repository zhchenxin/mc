package top.chenxin.mc.web.controller;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.service.TopicService;
import top.chenxin.mc.web.request.customer.ListForm;
import top.chenxin.mc.web.request.customer.CreateForm;
import top.chenxin.mc.web.response.SuccessResponse;
import top.chenxin.mc.service.CustomerService;
import top.chenxin.mc.web.response.customer.ListResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("customer")
public class CustomerController extends BaseController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TopicService topicService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map list(@Validated ListForm form) {
        Page<Customer> customerList = customerService.search(form.getTopicId(), form.getPage(), form.getLimit());

        if (customerList.isEmpty()) {
            return new ListResponse(customerList, null).toMap();
        }

        List<Long> topicIds = customerList.stream().map(Customer::getTopicId).collect(Collectors.toList());
        Utils.removeDuplicate(topicIds);

        List<Topic> topicList = topicService.getByIds(topicIds);

        return new ListResponse(customerList, topicList).toMap();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    private Map create(@Validated CreateForm form) {
        customerService.insert(form.getTopicId(), form.getName(), form.getApi(), form.getTimeout(), form.getAttempts());
        return new SuccessResponse().toMap();
    }

}
