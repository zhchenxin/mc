package top.chenxin.mc.service.impl;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.dao.*;
import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.entity.FailedMessage;
import top.chenxin.mc.entity.Message;
import top.chenxin.mc.entity.MessageLog;
import top.chenxin.mc.service.MessageService;
import top.chenxin.mc.service.exception.ServiceException;

import java.util.List;

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

    @Override
    public Page<Message> search(Long customerId, Integer status, Integer page, Integer limit) {
        return  messageDao.search(customerId, status, page, limit);
    }

    @Override
    public Message getById(Long id) {
        return messageDao.getById(id);
    }

    @Override
    public List<MessageLog> getMessageLogs(Long id) {
        return messageLogDao.getByMessageId(id);
    }

    @Override
    @Transactional
    public Message popMessage() {
        // 取出消息
        Message message = messageDao.popMessage();
        if (message == null) {
            return null;
        }

        // 修改消息的状态和超时时间
        Customer customer = customerDao.getById(message.getCustomerId());

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
        log.setRequest(message.getMessage());
        log.setResponse(response);
        log.setCustomerId(message.getCustomerId());
        log.setMessageId(message.getId());
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
        log.setError(error);
        log.setCustomerId(message.getCustomerId());
        log.setMessageId(message.getId());
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

    public void retryTimeoutMessage() {
        messageDao.retryTimeoutMessage();
    }
}
