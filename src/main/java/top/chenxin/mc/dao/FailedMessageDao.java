package top.chenxin.mc.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import top.chenxin.mc.entity.FailedMessage;

public interface FailedMessageDao {
    // 创建数据
    Long insert(FailedMessage message);

    void delete(Long id);

    // 搜索
    Page<FailedMessage> search(@Param("customerId") Long customerId, @Param("page") int page, @Param("limit") int limit);

    // 按照特定条件查找
    FailedMessage getById(@Param("id") Long id);

}
