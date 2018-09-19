package top.chenxin.mc.service;


import com.github.pagehelper.Page;
import top.chenxin.mc.entity.Topic;

import java.util.List;

public interface TopicService {

    void insert(String name, String description);

    void update(Long id, String name, String description);

    void delete(Long id);

    /**
     * 搜索
     * @param topicId 默认值 0
     */
    Page<Topic> search(Long topicId, Integer page, Integer limit);

    List<Topic> getByIds(List<Long> ids);

    Topic getById(Long id);

    List<Topic> getAll();

    /**
     * 向指定的 topic 中发送消息
     * @param topicName topic 名称
     * @param message 消息内容
     * @param delay 消息延迟时间, 单位秒
     */
    void push(String topicName, String message, Integer delay);

    void push(Long topicId, String message, Integer delay);
}
