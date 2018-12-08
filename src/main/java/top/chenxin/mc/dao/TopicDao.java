package top.chenxin.mc.dao;

import org.apache.ibatis.annotations.Param;
import top.chenxin.mc.entity.Topic;

import java.util.List;

public interface TopicDao {

    // 创建数据
    Long insert(Topic topic);

    void update(Topic topic);

    void delete(Long id);

    // 搜索
    List<Topic> getList(@Param("ids") List<Long> ids);

    // 按照特定条件查找
    Topic getByName(@Param("name") String name);

    Topic getById(@Param("id") Long id);

    List<Topic> getByIds(@Param("ids") List<Long> ids);
}
