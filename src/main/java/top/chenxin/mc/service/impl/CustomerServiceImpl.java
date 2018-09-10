package top.chenxin.mc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.chenxin.mc.form.customer.CreateForm;
import top.chenxin.mc.service.CustomerService;
import top.chenxin.mc.mapper.CustomerMapper;
import top.chenxin.mc.mapper.TopicMapper;
import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.form.customer.ListForm;
import top.chenxin.mc.lib.Utils;
import top.chenxin.mc.resource.CustomerCollection;

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
    public CustomerCollection search(ListForm listForm) {
        CustomerCollection collection = new CustomerCollection();
        collection.setPage(listForm.getPage());
        collection.setLimit(listForm.getLimit());
        collection.setCount(customerMapper.searchCount(listForm));
        collection.setList(customerMapper.search(listForm));

        if (collection.getList().size() == 0) {
            return collection;
        }

        List<Long> topicIds = collection.getList().stream().map(Customer::getTopicId).collect(Collectors.toList());
        Utils.removeDuplicate(topicIds);

        collection.setTopicList(topicMapper.getByIds(topicIds));

        return collection;
    }
}
