package top.chenxin.mc.dao;

import com.github.pagehelper.Page;
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
    Page<Customer> search(@Param("topicId") Long topicId, @Param("ids") Long ids, @Param("page") int page, @Param("limit") int limit);

    // 根据指定条件查询
    List<Customer> getByTopicId(@Param("topicId") Long topicId);

    Customer getById(@Param("id") Long id);
}
