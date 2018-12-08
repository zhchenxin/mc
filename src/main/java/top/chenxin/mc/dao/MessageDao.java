package top.chenxin.mc.dao;

import org.apache.ibatis.annotations.Param;
import top.chenxin.mc.entity.Message;

public interface MessageDao {
    // 创建数据
    Long insert(Message message);

    void update(Message message);

    void delete(Long id);

    // 按照特定条件查找
    Message getById(@Param("id") Long id);

    Message popMessage();

    // 操作
    void retryTimeoutMessage();
}
