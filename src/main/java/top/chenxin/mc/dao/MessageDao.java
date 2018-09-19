package top.chenxin.mc.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import top.chenxin.mc.entity.Message;

public interface MessageDao {
    // 创建数据
    Long insert(Message message);

    void update(Message message);

    void delete(Long id);

    // 搜索
    Page<Message> search(@Param("customerId") Long customerId, @Param("status") Integer status, @Param("page") int page, @Param("limit") int limit);

    // 按照特定条件查找
    Message getById(@Param("id") Long id);

    Message popMessage();

    // 操作
    void retryTimeoutMessage();
}
