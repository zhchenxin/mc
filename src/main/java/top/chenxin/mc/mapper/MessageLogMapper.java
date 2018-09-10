package top.chenxin.mc.mapper;

import org.apache.ibatis.annotations.Param;
import top.chenxin.mc.entity.MessageLog;

import java.util.List;

public interface MessageLogMapper {
    // 创建数据
    void create(MessageLog log);

    // 按照特定条件查找
    List<MessageLog> getByMessageId(@Param("messageId") Long messageId);
}
