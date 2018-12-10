package top.chenxin.mc.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import top.chenxin.mc.common.constant.ErrorCode;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.dao.*;
import top.chenxin.mc.entity.*;
import top.chenxin.mc.service.model.MessageModel;
import top.chenxin.mc.service.MessageService;
import top.chenxin.mc.service.exception.ServiceException;
import top.chenxin.mc.service.model.PageList;
import top.chenxin.mc.service.queue.Queue;

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
    public PageList<MessageLog> getMessageLogPage(Long customerId, Long messageId, Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        return new PageList<>((Page<MessageLog>) this.messageLogDao.getList(customerId, messageId));
    }

    @Override
    public PageList<FailedMessage> getFailedMessageList(Long customerId, Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        return new PageList<>((Page<FailedMessage>) this.failedMessageDao.getList(customerId));
    }

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
    public void migrate() {
        queue.migrate();
    }

    @Override
    public MessageModel pop() {
        return queue.pop();
    }

    @Override
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
        if (!message.getIsLog()) {
            return;
        }
        MessageLog log = new MessageLog();
        log.setMessageId(message.getId());
        log.setError(error);
        log.setCustomerId(message.getCustomerId());
        log.setTopicId(message.getTopicId());
        log.setTime(time);
        log.setRequest(message.getMessage());
        log.setResponse(response);
        log.setCreateDate(Utils.getCurrentTimestamp());
        messageLogDao.insert(log);
    }
}
