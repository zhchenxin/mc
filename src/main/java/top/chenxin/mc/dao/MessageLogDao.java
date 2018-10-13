package top.chenxin.mc.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import top.chenxin.mc.entity.MessageLog;

import java.util.List;

public interface MessageLogDao {
    // 创建数据
    void insert(MessageLog log);

    // 搜索
    Page<MessageLog> search(@Param("customerId") Long customerId, @Param("messageId") Long messageId, @Param("page") int page, @Param("limit") int limit);
}
