package top.chenxin.mc.service;

import top.chenxin.mc.core.ResourceCollection;
import top.chenxin.mc.resource.CustomerResource;

public interface CustomerService {

    void insert(Long topicId, String name, String api, Integer timeout, Integer attempts);

    /**
     * 更新数据, 如果参数为空, 表示此字段不更新
     */
    void update(Long id, String name, String api, Integer timeout, Integer attempts);

    void delete(Long id);

    /**
     * 按照指定参数进行筛选, 如果参数为默认值, 则不筛选此参数
     * @param topicId 默认值为 0
     */
    ResourceCollection<CustomerResource> search(Long topicId, Integer page, Integer limit);

    CustomerResource getById(Long id);
}
