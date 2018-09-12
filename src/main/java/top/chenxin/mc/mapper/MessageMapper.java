package top.chenxin.mc.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import top.chenxin.mc.request.message.ListForm;
import top.chenxin.mc.entity.Message;

public interface MessageMapper {
    // 创建数据
    Long create(Message message);

    // 搜索
    Page<Message> search(@Param("search") ListForm listForm, @Param("page") int page, @Param("limit") int limit);

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
