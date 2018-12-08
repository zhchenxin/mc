package top.chenxin.mc.service;


import top.chenxin.mc.entity.Cron;
import top.chenxin.mc.service.model.PageList;

import java.util.List;

public interface CronService {

    void insert(String name, String description, String spec, Long topicId);

    void update(Long id, String name, String description, String spec);

    void delete(Long id);

    /**
     * 重启定时任务
     */
    void start(Long id);

    /**
     * 暂停定时任务
     */
    void stop(Long id);

    /**
     * 搜索
     * @param topicId 默认值 0
     */
    PageList<Cron> getPage(Long topicId, Integer page, Integer limit);

    /**
     * 获取所有正常状态的任务
     */
    List<Cron> getAllNormalCron();
}
