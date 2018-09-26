package top.chenxin.mc.service;

import com.github.pagehelper.Page;
import top.chenxin.mc.entity.FailedMessage;
import top.chenxin.mc.entity.Message;
import top.chenxin.mc.entity.MessageLog;

public interface MessageService {

    /**
     * 搜索消息执行的历史记录
     */
    Page<MessageLog> searchLog(Long customerId, Integer page, Integer limit);

    /**
     * 搜索失败的消息
     */
    Page<FailedMessage> searchFailed(Long customerId, Integer page, Integer limit);

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
     * 从所有消息中推出一条消息用于执行, 如果没有消息, 则返回 null
     */
    Message popMessage();

    /**
     * 消息执行成功的时候调用
     */
    void messageSuccess(Long messageId, String response, Integer time);

    /**
     * 消息执行失败的时候调用
     */
    void messageFiled(Long messageId, String error, Integer time);

    /**
     * 重试超时的消息
     */
    void retryTimeoutMessage();
}
