package top.chenxin.mc.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.request.customer.ListForm;

import java.util.List;

public interface CustomerDao {

    // 创建数据
    Long create(Customer customer);

    // 搜索
    Page<Customer> search(@Param("search") ListForm listForm, @Param("page") int page, @Param("limit") int limit);

    // 根据指定条件查询
    List<Customer> getByTopicId(@Param("topicId") Long topicId);

    Customer getById(@Param("id") Long id);

    List<Customer> getByIds(@Param("ids") List<Long> ids);

}
