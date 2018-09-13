package top.chenxin.mc.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import top.chenxin.mc.entity.Topic;

import java.util.List;

public interface TopicDao {

    // 创建数据
    Long insert(Topic topic);

    // 搜索
    Page<Topic> search(@Param("topicId") Long topicId, @Param("page") int page, @Param("limit") int limit);

    // 按照特定条件查找
    Topic getByName(@Param("name") String name);

    Topic getById(@Param("id") Long id);

    List<Topic> getByIds(@Param("ids") List<Long> ids);
}
