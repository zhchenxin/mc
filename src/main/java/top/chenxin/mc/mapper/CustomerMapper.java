package top.chenxin.mc.mapper;

import org.apache.ibatis.annotations.Param;
import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.form.customer.ListForm;

import java.util.List;

public interface CustomerMapper {

    // 创建数据
    Long create(Customer customer);

    // 搜索
    List<Customer> search(ListForm listForm);

    Long searchCount(ListForm listForm);

    // 根据指定条件查询
    List<Customer> getByTopicId(@Param("topicId") Long topicId);

    Customer getById(@Param("id") Long id);

    List<Customer> getByIds(@Param("ids") List<Long> ids);

}
