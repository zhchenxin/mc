package top.chenxin.mc.service;

import top.chenxin.mc.core.ResourceCollection;
import top.chenxin.mc.resource.FailedMessageResource;
import top.chenxin.mc.resource.MessageLogResource;

public interface MessageService {

    /**
     * 消息执行日志
     */
    ResourceCollection<MessageLogResource> getMessageLogList(Long customerId, Integer page, Integer limit);

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
     * @param id
     */
    void deleteFailedMessage(Long id);

    /**
     * 从消息中取出一条可执行的, 执行消息
     * 消息执行成功, 删除消息
     * 消息执行失败, 如果没有超过重试次数, 则此消息会重试, 如果超过了重试次数, 删除消息并将消息设置为失败消息
     * @return 如果能取出消息则返回 true, 否则 false
     */
    boolean popMessage();

    /**
     * 重试超时的消息
     */
    void retryTimeoutMessage();
}
