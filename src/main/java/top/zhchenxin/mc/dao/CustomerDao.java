package top.zhchenxin.mc.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import top.zhchenxin.mc.entity.Customer;
import top.zhchenxin.mc.form.customer.ListForm;

import java.util.List;

public interface CustomerDao {

    // 创建数据
    void create(Customer customer);

    // 搜索
    List<Customer> search(ListForm listForm);

    Long searchCount(ListForm listForm);

    // 根据指定条件查询
    @Select("select * from customers where topic_id = #{topicId}")
    List<Customer> getByTopic(@Param("topicId") Long topicId);

    @Select("select * from customers where id = #{id}")
    Customer getById(@Param("id") Long id);

    List<Customer> getByIds(@Param("ids") List<Long> ids);

}
