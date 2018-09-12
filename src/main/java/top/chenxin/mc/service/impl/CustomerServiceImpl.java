package top.chenxin.mc.service.impl;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.request.customer.CreateForm;
import top.chenxin.mc.response.EmptyPaginationResponse;
import top.chenxin.mc.response.Response;
import top.chenxin.mc.service.CustomerService;
import top.chenxin.mc.mapper.CustomerMapper;
import top.chenxin.mc.mapper.TopicMapper;
import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.request.customer.ListForm;
import top.chenxin.mc.lib.Utils;
import top.chenxin.mc.response.customer.ListResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public void create(CreateForm form) {
        customerMapper.create(form.toCustomer());
    }

    @Override
    public Response search(ListForm listForm) {

        Page<Customer> customerList = customerMapper.search(listForm, listForm.getPage(), listForm.getLimit());

        if (customerList.size() == 0) {
            return new EmptyPaginationResponse(customerList);
        }

        List<Long> topicIds = customerList.stream().map(Customer::getTopicId).collect(Collectors.toList());
        Utils.removeDuplicate(topicIds);

        List<Topic> topicList = topicMapper.getByIds(topicIds);

        ListResponse response = new ListResponse(customerList, topicList);
        response.setPage(customerList);
        return response;
    }
}
