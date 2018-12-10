package top.chenxin.mc.dao;

import org.apache.ibatis.annotations.Param;
import top.chenxin.mc.entity.MessageMinCount;

import java.util.List;

public interface MessageMinCountDao {
    // 创建数据
    Long insert(MessageMinCount message);

    // 搜索
    List<MessageMinCount> getList(@Param("start") Integer start, @Param("end") Integer end);
}
