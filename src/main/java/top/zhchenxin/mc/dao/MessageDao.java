package top.zhchenxin.mc.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.zhchenxin.mc.entity.Message;
import top.zhchenxin.mc.form.message.ListForm;

import java.util.List;

public interface MessageDao {
    // 创建数据
    void create(Message message);

    // 搜索
    List<Message> search(ListForm listForm);

    Long searchCount(ListForm listForm);

    // 按照特定条件查找
    @Select("select * from messages where id = #{id}")
    Message getById(@Param("id") Long id);

    @Select("select * from messages where message_id = #{messageId} and customer_id = #{customerId}")
    Message getByMessageIdAndCustomerId(@Param("messageId") Long messageId, @Param("customerId") Long customerId);

    // 操作
    @Update("UPDATE `messages` SET status=1, attempts = attempts + 1 where timeout_date < unix_timestamp(now()) and status=2")
    void retryTimeoutMessage();

    @Select("SELECT * FROM messages WHERE status = 1 AND available_date < unix_timestamp(now()) limit 1 for update")
    Message popMessage();

    @Update("UPDATE `messages` SET timeout_date=${timeoutDate},status=2 WHERE id=${id}")
    void start(@Param("id") Long id, @Param("timeoutDate") Integer timeoutDate);

    @Update("UPDATE `messages` SET status=4 WHERE id=${id}")
    void failed(@Param("id") Long id);

    @Update("UPDATE `messages` SET status=1, attempts = attempts + 1 WHERE id=${id}")
    void retry(@Param("id") Long id);

    @Update("UPDATE `messages` SET status=3 WHERE id=${id}")
    void success(@Param("id") Long id);
}
