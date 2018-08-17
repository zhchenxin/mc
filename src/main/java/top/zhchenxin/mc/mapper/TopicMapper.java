package top.zhchenxin.mc.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.zhchenxin.mc.entity.Topic;
import top.zhchenxin.mc.form.topic.ListForm;

import java.util.List;

public interface TopicMapper {

    // 创建数据
    @Insert(" INSERT INTO topics SET name=#{name},description=#{description},create_date=unix_timestamp(now())")
    Long create(Topic topic);

    // 搜索
    List<Topic> search(ListForm listForm);

    Long searchCount(ListForm listForm);

    // 按照特定条件查找
    @Select("select * from topics where name = #{name} limit 1")
    Topic getByName(@Param("name") String name);

    @Select("select * from topics where id = #{id}")
    Topic getById(@Param("id") Long id);

    List<Topic> getByIds(@Param("ids") List<Long> ids);
}