package top.chenxin.mc.service.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.dao.CustomerDao;
import top.chenxin.mc.dao.MessageDao;
import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.service.model.MessageModel;
import top.chenxin.mc.entity.Message;

/**
 * 数据库队列
 */
//@Service
public class DatabaseQueue implements Queue {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private CustomerDao customerDao;

    @Override
    @Transactional
    public MessageModel pop() {
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
            return null;
        }

        Message update = new Message();
        update.setId(message.getId());
        update.setTimeoutDate(Utils.getCurrentTimestamp() + customer.getTimeout());
        update.setStatus(Message.StatusRunning);
        update.setAttempts(message.getAttempts() + 1);
        messageDao.update(update);

        // 将 message 包装成 model
        return new MessageModel(message, customer);
    }

    @Override
    public void push(MessageModel message) {
        Message msg = new Message();
        msg.setId(msg.getId());
        msg.setTopicId(message.getTopicId());
        msg.setCustomerId(message.getCustomerId());
        msg.setMessage(message.getMessage());
        msg.setAvailableDate(Utils.getCurrentTimestamp() + message.getDelay());
        msg.setStatus(Message.StatusWatting);
        msg.setAttempts(message.getAttempts());
        msg.setPriority(message.getPriority());
        messageDao.insert(msg);
    }

    @Override
    public void migrate() {
        messageDao.retryTimeoutMessage();
    }

    @Override
    public void success(MessageModel message) {
        // 移除消息
        messageDao.delete(message.getId());
    }

    @Override
    public void failed(MessageModel message) {
        if (needRetry(message)) {
            // 重新放入数据库中
            Message update = new Message();
            update.setId(message.getId());
            update.setStatus(Message.StatusWatting);
            messageDao.update(update);
        } else {
            // 删除消息
            messageDao.delete(message.getId());
        }
    }
}
