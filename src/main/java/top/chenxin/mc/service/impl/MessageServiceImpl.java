package top.chenxin.mc.service.impl;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import top.chenxin.mc.common.constant.ErrorCode;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.core.ResourceCollection;
import top.chenxin.mc.dao.*;
import top.chenxin.mc.entity.*;
import top.chenxin.mc.model.MessageModel;
import top.chenxin.mc.resource.FailedMessageResource;
import top.chenxin.mc.resource.MessageLogResource;
import top.chenxin.mc.service.MessageService;
import top.chenxin.mc.service.exception.ServiceException;
import top.chenxin.mc.service.queue.Queue;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private MessageLogDao messageLogDao;

    @Autowired
    private FailedMessageDao failedMessageDao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private Queue queue;

    @Override
    public ResourceCollection<MessageLogResource> getMessageLogList(Long customerId, Long messageId, Integer page, Integer limit) {
        Page<MessageLog> logs = this.messageLogDao.search(customerId, messageId, page, limit);

        List<MessageLogResource> resources = new ArrayList<>();
        for (MessageLog log : logs) {
            resources.add(new MessageLogResource(log));
        }

        return new ResourceCollection<>(resources, logs);
    }

    @Override
    public ResourceCollection<FailedMessageResource> getFailedMessageList(Long customerId, Integer page, Integer limit) {
        Page<FailedMessage> messages = this.failedMessageDao.search(customerId, page, limit);

        List<FailedMessageResource> resources = new ArrayList<>();
        for (FailedMessage message : messages) {
            resources.add(new FailedMessageResource(message));
        }

        return new ResourceCollection<>(resources, messages);
    }

    @Override
    @Transactional
    public void retryMessage(Long id) {
        FailedMessage message = failedMessageDao.getById(id);
        if (message == null) {
            throw new ServiceException("消息未找到", ErrorCode.OBJECT_NOT_DOUND);
        }

        // 从失败表中删除
        failedMessageDao.delete(id);

        Customer customer = customerDao.getById(message.getCustomerId());

        // 添加消息到 message 表中
        queue.push(new MessageModel(message, customer, 0));
    }

    @Override
    public void deleteFailedMessage(Long id) {
        failedMessageDao.delete(id);
    }

    @Override
    public void retryTimeoutMessage() {
        queue.retryTimeout();
    }

    @Override
    @Transactional
    public MessageModel pop() {
        return queue.pop();
    }

    @Override
    @Transactional
    public void messageSuccess(MessageModel message, String response, Integer time) {
        // 生成执行日志
        insertMessageLog(message, "", response, time);

        // 通知消息队列
        queue.success(message);
    }

    @Override
    @Transactional
    public void messageFiled(MessageModel message, String error, Integer time) {

        // 生成执行日志
        insertMessageLog(message, error, "", time);

        if (!queue.needRetry(message)) {
            // 记录到失败表数据
            FailedMessage failedMessage = new FailedMessage();
            failedMessage.setId(message.getId());
            failedMessage.setTopicId(message.getTopicId());
            failedMessage.setCustomerId(message.getCustomerId());
            failedMessage.setMessage(message.getMessage());
            failedMessage.setError(error);
            failedMessage.setAttempts(message.getAttempts());
            failedMessageDao.insert(failedMessage);
        }

        // 通知消息队列
        queue.failed(message);
    }

    private void insertMessageLog(MessageModel message, String error, String response, Integer time){
        MessageLog log = new MessageLog();
        log.setMessageId(message.getId());
        log.setError(error);
        log.setCustomerId(message.getCustomerId());
        log.setTopicId(message.getTopicId());
        log.setTime(time);
        log.setRequest(message.getMessage());
        log.setResponse(response);
        messageLogDao.insert(log);
    }
}
