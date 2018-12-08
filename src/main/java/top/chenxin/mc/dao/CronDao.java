package top.chenxin.mc.dao;

import org.apache.ibatis.annotations.Param;
import top.chenxin.mc.entity.Cron;

import java.util.List;

public interface CronDao {

    // 创建数据
    Long insert(Cron cron);

    void update(Cron cron);

    void delete(Long id);

    // 搜索
    List<Cron> getList(@Param("topicId") Long topicId);

    Cron getById(Long id);

    List<Cron> getAllNormalCron();
}
