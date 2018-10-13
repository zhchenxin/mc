package top.chenxin.mc.service.impl;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.chenxin.mc.core.ResourceCollection;
import top.chenxin.mc.entity.FailedMessage;
import top.chenxin.mc.resource.CustomerResource;
import top.chenxin.mc.resource.FailedMessageResource;
import top.chenxin.mc.service.CustomerService;
import top.chenxin.mc.dao.CustomerDao;
import top.chenxin.mc.dao.TopicDao;
import top.chenxin.mc.entity.Customer;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private CustomerDao customerDao;

    @Override
    public void insert(Long topicId, String name, String api, Integer timeout, Integer attempts) {
        Customer item = new Customer();
        item.setTopicId(topicId);
        item.setName(name);
        item.setApi(api);
        item.setAttempts(attempts);
        item.setTimeout(timeout);
        customerDao.insert(item);
    }

    @Override
    public void update(Long id, String name, String api, Integer timeout, Integer attempts) {
        Customer item = new Customer();
        item.setId(id);
        item.setName(name);
        item.setApi(api);
        item.setAttempts(attempts);
        item.setTimeout(timeout);
        customerDao.update(item);
    }

    @Override
    public void delete(Long id) {
        customerDao.delete(id);
    }

    @Override
    public ResourceCollection<CustomerResource> search(List<Long> ids, Long topicId, Integer page, Integer limit) {
        Page<Customer> customers = this.customerDao.search(topicId, ids, page, limit);

        List<CustomerResource> resources = new ArrayList<>();
        for (Customer customer : customers) {
            resources.add(new CustomerResource(customer));
        }

        return new ResourceCollection<>(resources, customers);
    }

    @Override
    public CustomerResource getById(Long id) {
        Customer customer = this.customerDao.getById(id);
        return new CustomerResource(customer);
    }
}
