package top.chenxin.mc.dao;

import org.apache.ibatis.annotations.Param;
import top.chenxin.mc.entity.Customer;

import java.util.List;

public interface CustomerDao {

    // 创建数据
    Long insert(Customer customer);

    // 创建数据
    void update(Customer customer);

    void delete(Long id);

    // 搜索
    List<Customer> getList(@Param("topicId") Long topicId, @Param("ids") List<Long> ids);

    // 根据指定条件查询
    List<Customer> getByTopicId(@Param("topicId") Long topicId);

    Customer getById(@Param("id") Long id);
}
