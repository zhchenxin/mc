package top.chenxin.mc.dao;

import org.apache.ibatis.annotations.Param;
import top.chenxin.mc.entity.MessageLog;

import java.util.List;

public interface MessageLogDao {
    // 创建数据
    void insert(MessageLog log);

    // 按照特定条件查找
    List<MessageLog> getByMessageId(@Param("messageId") Long messageId);
}
