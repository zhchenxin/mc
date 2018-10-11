package top.chenxin.mc.service.impl;

import com.github.pagehelper.Page;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import top.chenxin.mc.common.constant.ErrorCode;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.core.ResourceCollection;
import top.chenxin.mc.dao.*;
import top.chenxin.mc.entity.*;
import top.chenxin.mc.resource.FailedMessageResource;
import top.chenxin.mc.resource.MessageLogResource;
import top.chenxin.mc.resource.TopicResource;
import top.chenxin.mc.service.MessageService;
import top.chenxin.mc.service.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;

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

    @Override
    public ResourceCollection<MessageLogResource> getMessageLogList(Long customerId, Integer page, Integer limit) {
        Page<MessageLog> logs = this.messageLogDao.search(customerId, page, limit);

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

        // 添加消息到 message 表中
        Message msg = new Message();
        msg.setTopicId(message.getTopicId());
        msg.setCustomerId(message.getCustomerId());
        msg.setMessage(message.getMessage());
        msg.setAvailableDate(Utils.getCurrentTimestamp());
        msg.setStatus(Message.StatusWatting);
        msg.setAttempts(0);
        messageDao.insert(msg);
    }

    @Override
    public void deleteFailedMessage(Long id) {
        failedMessageDao.delete(id);
    }

    public void retryTimeoutMessage() {
        messageDao.retryTimeoutMessage();
    }

    @Override
    @Transactional
    public Message pop() {
        // 取出消息
        Message message = messageDao.popMessage();
        if (message == null) {
            return null;
        }

        // 修改消息的状态和超时时间
        Customer customer = customerDao.getById(message.getCustomerId());
        if (customer == null) {
            // 如果消息的消费者被删除, 则需要删除对应消息
            messageDao.delete(message.getId());
        }

        Message update = new Message();
        update.setId(message.getId());
        update.setTimeoutDate(Utils.getCurrentTimestamp() + customer.getTimeout());
        update.setStatus(Message.StatusRunning);
        messageDao.update(update);
        return message;
    }

    @Override
    @Transactional
    public void messageSuccess(Long messageId, String response, Integer time) {
        Message message = messageDao.getById(messageId);

        if (!message.getStatus().equals(Message.StatusRunning)) {
            throw new ServiceException("消息状态异常");
        }

        // 生成执行日志
        MessageLog log = new MessageLog();
        log.setMessageId(0L); // 消息即将删除, 所以不需要消息id
        log.setRequest(message.getMessage());
        log.setResponse(response);
        log.setCustomerId(message.getCustomerId());
        log.setTopicId(message.getTopicId());
        log.setTime(time);
        log.setError("");
        messageLogDao.insert(log);

        // 移除消息
        messageDao.delete(messageId);
    }

    @Override
    @Transactional
    public void messageFiled(Long messageId, String error, Integer time) {
        Message message = messageDao.getById(messageId);

        if (!message.getStatus().equals(Message.StatusRunning)) {
            throw new ServiceException("消息状态异常");
        }

        // 生成执行日志
        MessageLog log = new MessageLog();
        log.setMessageId(message.getId());
        log.setError(error);
        log.setCustomerId(message.getCustomerId());
        log.setTopicId(message.getTopicId());
        log.setTime(time);
        log.setRequest(message.getMessage());
        log.setResponse("");
        messageLogDao.insert(log);

        // 判断消息是否需要重试
        Customer customer = customerDao.getById(message.getCustomerId());
        if (message.getAttempts() >= customer.getAttempts()) {
            // 记录到失败表数据
            FailedMessage failedMessage = new FailedMessage();
            failedMessage.setId(message.getId());
            failedMessage.setTopicId(message.getTopicId());
            failedMessage.setCustomerId(message.getCustomerId());
            failedMessage.setMessage(message.getMessage());
            failedMessage.setError(error);
            failedMessage.setAttempts(message.getAttempts());
            failedMessageDao.insert(failedMessage);

            // 删除消息
            messageDao.delete(messageId);
        } else {
            Message update = new Message();
            update.setId(message.getId());
            update.setStatus(Message.StatusWatting);
            update.setAttempts(message.getAttempts() + 1);
            messageDao.update(update);
        }
    }
}
