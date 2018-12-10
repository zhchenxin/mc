package top.chenxin.mc.service;

import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.service.model.PageList;

import java.util.List;

public interface CustomerService {

    void insert(Long topicId, String name, String api, Integer timeout, Integer attempts, boolean isLog, Integer priority);

    /**
     * 更新数据, 如果参数为空, 表示此字段不更新
     */
    void update(Long id, String name, String api, Integer timeout, Integer attempts, boolean isLog, Integer priority);

    void delete(Long id);

    /**
     * 按照指定参数进行筛选, 如果参数为默认值, 则不筛选此参数
     */
    PageList<Customer> getPage(List<Long> ids, Long topicId, Integer page, Integer limit);

    Customer getById(Long id);
}
