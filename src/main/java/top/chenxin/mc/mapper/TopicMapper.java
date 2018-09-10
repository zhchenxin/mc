package top.chenxin.mc.mapper;

import org.apache.ibatis.annotations.Param;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.form.topic.ListForm;

import java.util.List;

public interface TopicMapper {

    // 创建数据
    Long create(Topic topic);

    // 搜索
    List<Topic> search(ListForm listForm);

    Long searchCount(ListForm listForm);

    // 按照特定条件查找
    Topic getByName(@Param("name") String name);

    Topic getById(@Param("id") Long id);

    List<Topic> getByIds(@Param("ids") List<Long> ids);
}
