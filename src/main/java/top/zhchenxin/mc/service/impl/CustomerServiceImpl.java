package top.zhchenxin.mc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zhchenxin.mc.dao.CustomerDao;
import top.zhchenxin.mc.dao.TopicDao;
import top.zhchenxin.mc.entity.Customer;
import top.zhchenxin.mc.form.customer.CreateForm;
import top.zhchenxin.mc.form.customer.ListForm;
import top.zhchenxin.mc.lib.Utils;
import top.zhchenxin.mc.resource.CustomerCollection;
import top.zhchenxin.mc.service.CustomerService;

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
        this.customerDao.create(form.toCustomer());
    }

    @Override
    public CustomerCollection search(ListForm listForm) {
        CustomerCollection collection = new CustomerCollection();
        collection.setPage(listForm.getPage());
        collection.setLimit(listForm.getLimit());
        collection.setCount(this.customerDao.searchCount(listForm));
        collection.setList(this.customerDao.search(listForm));

        if (collection.getList().size() == 0) {
            return collection;
        }

        List<Long> topicIds = collection.getList().stream().map(Customer::getTopicId).collect(Collectors.toList());
        Utils.removeDuplicate(topicIds);

        collection.setTopicList(this.topicDao.getByIds(topicIds));

        return collection;
    }

    public Customer getById(Long id) {
        return this.customerDao.getById(id);
    }
}
