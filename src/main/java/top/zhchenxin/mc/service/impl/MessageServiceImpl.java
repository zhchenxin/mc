package top.zhchenxin.mc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zhchenxin.mc.dao.CustomerDao;
import top.zhchenxin.mc.dao.MessageDao;
import top.zhchenxin.mc.dao.MessageLogDao;
import top.zhchenxin.mc.dao.TopicDao;
import top.zhchenxin.mc.entity.Customer;
import top.zhchenxin.mc.entity.Message;
import top.zhchenxin.mc.entity.MessageLog;
import top.zhchenxin.mc.form.message.ListForm;
import top.zhchenxin.mc.lib.Utils;
import top.zhchenxin.mc.resource.MessageCollection;
import top.zhchenxin.mc.service.MessageService;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public MessageCollection search(ListForm listForm) {
        MessageCollection collection = new MessageCollection();
        collection.setPage(listForm.getPage());
        collection.setLimit(listForm.getLimit());
        collection.setCount(this.messageDao.searchCount(listForm));
        collection.setList(this.messageDao.search(listForm));

        if (collection.getList().size() == 0) {
            return collection;
        }

        List<Long> customerIds = collection.getList().stream().map(Message::getCustomerId).collect(Collectors.toList());
        List<Long> topicIds = collection.getList().stream().map(Message::getTopicId).collect(Collectors.toList());
        Utils.removeDuplicate(customerIds);
        Utils.removeDuplicate(topicIds);

        collection.setCustomerList(this.customerDao.getByIds(customerIds));
        collection.setTopicList(this.topicDao.getByIds(topicIds));

        return collection;
    }

    @Override
    @Transactional
    public Message pop() {
        Message message = this.messageDao.popMessage();
        if (message == null) {
            return null;
        }
        Customer customer = this.customerDao.getById(message.getCustomerId());
        int time = (int) (System.currentTimeMillis() / 1000);
        this.messageDao.start(message.getId(), customer.getTimeout() + time);
        return message;
    }

    @Override
    @Transactional
    public void messageSuccess(Long id, String response, Integer time) {
        // 生成执行日志
        Message message = this.messageDao.getById(id);
        MessageLog log = new MessageLog();
        log.setCreateDate((int)(System.currentTimeMillis() / 1000));
        log.setResponse(response);
        log.setCustomerId(message.getCustomerId());
        log.setMessageId(message.getId());
        log.setTopicId(message.getTopicId());
        log.setTime(time);
        this.messageLogDao.createSuccessLog(log);

        // 修改消息状态
        this.messageDao.success(id);
    }

    @Override
    public void messageFiled(Long id, String error, Integer time) {
        // 生成执行日志
        Message message = this.messageDao.getById(id);
        MessageLog log = new MessageLog();
        log.setCreateDate((int)(System.currentTimeMillis() / 1000));
        log.setError(error);
        log.setCustomerId(message.getCustomerId());
        log.setMessageId(message.getId());
        log.setTopicId(message.getTopicId());
        log.setTime(time);
        this.messageLogDao.createErrorLog(log);

        Customer customer = this.customerDao.getById(message.getCustomerId());
        if (message.getAttempts() >= customer.getAttempts()) {
            // 如果
            this.messageDao.failed(id);
        } else {
            this.messageDao.retry(id);
        }
    }

    public void retryTimeoutMessage() {
        this.messageDao.retryTimeoutMessage();
    }
}
