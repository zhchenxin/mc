package top.chenxin.mc.service;

import top.chenxin.mc.core.ResourceCollection;
import top.chenxin.mc.entity.Message;
import top.chenxin.mc.resource.FailedMessageResource;
import top.chenxin.mc.resource.MessageLogResource;

public interface MessageService {

    /**
     * 消息执行日志
     */
    ResourceCollection<MessageLogResource> getMessageLogList(Long customerId, Long messageId, Integer page, Integer limit);

    /**
     * 失败消息
     */
    ResourceCollection<FailedMessageResource> getFailedMessageList(Long customerId, Integer page, Integer limit);

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
     * 重试超时的消息
     */
    void retryTimeoutMessage();

    /**
     * 从消息中 pop 出一条消息
     */
    Message pop();

    /**
     * 消息执行成功
     */
    void messageSuccess(Long messageId, String response, Integer time);

    /**
     * 消息执行失败
     */
    void messageFiled(Long messageId, String error, Integer time);
}
