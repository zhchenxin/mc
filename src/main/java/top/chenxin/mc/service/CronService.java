package top.chenxin.mc.service;


import com.github.pagehelper.Page;
import top.chenxin.mc.entity.Cron;

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
    Page<Cron> search(Long topicId, Integer page, Integer limit);

    List<Cron> getAll();

    Cron getById(Long id);
}
