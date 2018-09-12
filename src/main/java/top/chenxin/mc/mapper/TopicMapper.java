package top.chenxin.mc.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.request.topic.ListForm;

import java.util.List;

public interface TopicMapper {

    // 创建数据
    Long create(Topic topic);

    // 搜索
    Page<Topic> search(@Param("search") ListForm listForm, @Param("page") int page, @Param("limit") int limit);

    // 按照特定条件查找
    Topic getByName(@Param("name") String name);

    Topic getById(@Param("id") Long id);

    List<Topic> getByIds(@Param("ids") List<Long> ids);
}
