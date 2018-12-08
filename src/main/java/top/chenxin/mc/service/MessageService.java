package top.chenxin.mc.service;

import top.chenxin.mc.entity.FailedMessage;
import top.chenxin.mc.entity.MessageLog;
import top.chenxin.mc.service.model.MessageModel;
import top.chenxin.mc.service.model.PageList;

public interface MessageService {

    /**
     * 消息执行日志
     */
    PageList<MessageLog> getMessageLogPage(Long customerId, Long messageId, Integer page, Integer limit);

    /**
     * 失败消息
     */
    PageList<FailedMessage> getFailedMessageList(Long customerId, Integer page, Integer limit);

    /**
     * 重试消息
     * @param id FailedMessage中的id
     */
    void retryMessage(Long id);

    /**
     * 删除失败消息
     * @param id 消息id
     */
    void deleteFailedMessage(Long id);

    /**
     * 数据合并。将过期、延迟等消息合并到消息队列中
     */
    void migrate();

    /**
     * 从消息中 pop 出一条消息
     */
    MessageModel pop();

    /**
     * 消息执行成功
     */
    void messageSuccess(MessageModel message, String response, Integer time);

    /**
     * 消息执行失败
     */
    void messageFiled(MessageModel message, String error, Integer time);
}
