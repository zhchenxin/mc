package top.chenxin.mc.service;


import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.service.model.PageList;

import java.util.List;

public interface TopicService {

    void insert(String name, String description);

    void update(Long id, String name, String description);

    void delete(Long id);

    /**
     * 获取 topic 资源
     * @param ids topic id 如果为null, 则表示不指定该搜索条件
     */
    PageList<Topic> getPage(List<Long> ids, Integer page, Integer limit);


}
