package top.chenxin.mc.service;

import com.github.pagehelper.Page;
import top.chenxin.mc.entity.Customer;

import java.util.List;

public interface CustomerService {

    void insert(Long topicId, String name, String api, Integer timeout, Integer attempts);

    /**
     * 按照指定参数进行筛选, 如果参数为默认值, 则不筛选此参数
     * @param topicId 默认值为 0
     */
    Page<Customer> search(Long topicId, Integer page, Integer limit);

    List<Customer> getByIds(List<Long> ids);

    Customer getById(Long id);
}
