package top.zhchenxin.mc.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.zhchenxin.mc.entity.MessageLog;

import java.util.List;

public interface MessageLogMapper {
    // 创建数据
    void createSuccessLog(MessageLog log);
    void createErrorLog(MessageLog log);

    // 按照特定条件查找

    @Select("select * from message_logs where message_id = #{messageId}")
    List<MessageLog> getByMessageId(@Param("messageId") Long messageId);
}
