package top.chenxin.mc.service.impl;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.request.customer.CreateForm;
import top.chenxin.mc.response.EmptyPaginationResponse;
import top.chenxin.mc.response.Response;
import top.chenxin.mc.service.CustomerService;
import top.chenxin.mc.dao.CustomerDao;
import top.chenxin.mc.dao.TopicDao;
import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.request.customer.ListForm;
import top.chenxin.mc.lib.Utils;
import top.chenxin.mc.response.customer.ListResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private CustomerDao customerDao;

    @Override
    public void create(CreateForm form) {
        customerDao.create(form.toCustomer());
    }

    @Override
    public Response search(ListForm listForm) {

        Page<Customer> customerList = customerDao.search(listForm, listForm.getPage(), listForm.getLimit());

        if (customerList.size() == 0) {
            return new EmptyPaginationResponse(customerList);
        }

        List<Long> topicIds = customerList.stream().map(Customer::getTopicId).collect(Collectors.toList());
        Utils.removeDuplicate(topicIds);

        List<Topic> topicList = topicDao.getByIds(topicIds);

        ListResponse response = new ListResponse(customerList, topicList);
        response.setPage(customerList);
        return response;
    }
}
