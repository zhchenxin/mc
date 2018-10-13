package top.chenxin.mc.service;


import top.chenxin.mc.core.ResourceCollection;
import top.chenxin.mc.resource.TopicResource;

import java.util.List;

public interface TopicService {

    void insert(String name, String description);

    void update(Long id, String name, String description);

    void delete(Long id);

    /**
     * 获取 topic 资源
     * @param ids topic id 如果为null, 则表示不指定该搜索条件
     */
    ResourceCollection<TopicResource> getList(List<Long> ids, Integer page, Integer limit);

    /**
     * 向指定的 topic 中发送消息
     * @param topicName topic 名称
     * @param message 消息内容
     * @param delay 消息延迟时间, 单位秒
     */
    void push(String topicName, String message, Integer delay);

    void push(Long topicId, String message, Integer delay);
}
