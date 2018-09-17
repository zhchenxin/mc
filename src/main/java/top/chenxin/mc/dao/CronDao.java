package top.chenxin.mc.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import top.chenxin.mc.entity.Cron;
import top.chenxin.mc.entity.Topic;

import java.util.List;

public interface CronDao {

    // 创建数据
    Long insert(Cron cron);

    void update(Cron cron);

    void delete(Long id);

    // 搜索
    Page<Cron> search(@Param("topicId") Long topicId, @Param("page") int page, @Param("limit") int limit);

    List<Cron> getAll();

    Cron getById(Long id);
}
