package top.chenxin.mc.dao;

import org.apache.ibatis.annotations.Param;
import top.chenxin.mc.entity.FailedMessage;

import java.util.List;

public interface FailedMessageDao {
    // 创建数据
    Long insert(FailedMessage message);

    void delete(Long id);

    // 搜索
    List<FailedMessage> getList(@Param("customerId") Long customerId);

    // 按照特定条件查找
    FailedMessage getById(@Param("id") Long id);

}
