package top.chenxin.mc.mapper;

import org.apache.ibatis.annotations.Param;
import top.chenxin.mc.form.message.ListForm;
import top.chenxin.mc.entity.Message;

import java.util.List;

public interface MessageMapper {
    // 创建数据
    Long create(Message message);

    // 搜索
    List<Message> search(ListForm listForm);

    Long searchCount(ListForm listForm);

    // 按照特定条件查找
    Message getById(@Param("id") Long id);

    Message getByMessageIdAndCustomerId(@Param("messageId") Long messageId, @Param("customerId") Long customerId);

    Message popMessage();

    // 操作
    void retryTimeoutMessage();

    void start(@Param("id") Long id, @Param("timeoutDate") Integer timeoutDate);

    void failed(@Param("id") Long id);

    void retry(@Param("id") Long id);

    void success(@Param("id") Long id);
}
