package top.chenxin.mc.service.impl;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.chenxin.mc.dao.po.Topic;
import top.chenxin.mc.service.dto.CustomerSearchList;
import top.chenxin.mc.service.CustomerService;
import top.chenxin.mc.dao.CustomerDao;
import top.chenxin.mc.dao.TopicDao;
import top.chenxin.mc.dao.po.Customer;
import top.chenxin.mc.common.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

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
    public CustomerSearchList search(Long topicId, Integer page, Integer limit) {

        Page<Customer> customerList = customerDao.search(topicId, page, limit);

        if (customerList.size() == 0) {
            return new CustomerSearchList(customerList, null);
        }

        List<Long> topicIds = customerList.stream().map(Customer::getTopicId).collect(Collectors.toList());
        Utils.removeDuplicate(topicIds);

        List<Topic> topicList = topicDao.getByIds(topicIds);

        return new CustomerSearchList(customerList, topicList);
    }
}
