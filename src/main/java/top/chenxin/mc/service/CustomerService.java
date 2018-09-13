package top.chenxin.mc.service;

import top.chenxin.mc.service.dto.CustomerSearchList;

public interface CustomerService {

    void insert(Long topicId, String name, String api, Integer timeout, Integer attempts);

    /**
     * 按照指定参数进行筛选, 如果参数为默认值, 则不筛选此参数
     * @param topicId 默认值为 0
     */
    CustomerSearchList search(Long topicId, Integer page, Integer limit);
}
