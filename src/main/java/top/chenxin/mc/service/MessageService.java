package top.chenxin.mc.service;


import com.github.pagehelper.Page;
import top.chenxin.mc.entity.Message;
import top.chenxin.mc.entity.MessageLog;

import java.util.List;

public interface MessageService {
    /**
     * 按照条件搜索
     * @param customerId 默认值0
     * @param status 默认值0
     */
    Page<Message> search(Long customerId, Integer status, Integer page, Integer limit);

    /**
     * 根据消息id获取消息内容
     * @param id 消息id
     */
    Message getById(Long id);

    /**
     * 获取消息的请求日志
     * @param id 消息id
     */
    List<MessageLog> getMessageLogs(Long id);
}
