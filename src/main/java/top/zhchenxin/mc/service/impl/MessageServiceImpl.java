package top.zhchenxin.mc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zhchenxin.mc.mapper.CustomerMapper;
import top.zhchenxin.mc.mapper.MessageMapper;
import top.zhchenxin.mc.mapper.MessageLogMapper;
import top.zhchenxin.mc.mapper.TopicMapper;
import top.zhchenxin.mc.entity.Customer;
import top.zhchenxin.mc.entity.Message;
import top.zhchenxin.mc.entity.MessageLog;
import top.zhchenxin.mc.form.message.ListForm;
import top.zhchenxin.mc.lib.Utils;
import top.zhchenxin.mc.resource.MessageCollection;
import top.zhchenxin.mc.resource.MessageDetail;
import top.zhchenxin.mc.service.MessageService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private MessageLogMapper messageLogMapper;

    @Override
    public MessageCollection search(ListForm listForm) {
        MessageCollection collection = new MessageCollection();
        collection.setPage(listForm.getPage());
        collection.setLimit(listForm.getLimit());
        collection.setCount(this.messageMapper.searchCount(listForm));
        collection.setList(this.messageMapper.search(listForm));

        if (collection.getList().size() == 0) {
            return collection;
        }

        List<Long> customerIds = collection.getList().stream().map(Message::getCustomerId).collect(Collectors.toList());
        List<Long> topicIds = collection.getList().stream().map(Message::getTopicId).collect(Collectors.toList());
        Utils.removeDuplicate(customerIds);
        Utils.removeDuplicate(topicIds);

        collection.setCustomerList(this.customerMapper.getByIds(customerIds));
        collection.setTopicList(this.topicMapper.getByIds(topicIds));

        return collection;
    }

    @Override
    public MessageDetail getDetailById(Long id) {
        MessageDetail detail = new MessageDetail();
        Message message = this.messageMapper.getById(id);
        if (message == null) {
            return null;
        }

        detail.setEntity(message);
        detail.setCustomer(this.customerMapper.getById(message.getCustomerId()));
        detail.setTopic(this.topicMapper.getById(message.getTopicId()));
        detail.setLogList(this.messageLogMapper.getByMessageId(message.getId()));
        return detail;
    }

    @Override
    @Transactional
    public MessageDetail pop() {
        Message message = this.messageMapper.popMessage();
        if (message == null) {
            return null;
        }
        Customer customer = customerMapper.getById(message.getCustomerId());
        messageMapper.start(message.getId(), Utils.getCurrentTimestamp() + customer.getTimeout());
        return this.getDetailById(message.getId());
    }

    @Override
    @Transactional
    public void messageSuccess(Long id, String response, Integer time) {
        // 生成执行日志
        Message message = this.messageMapper.getById(id);
        MessageLog log = new MessageLog();
        log.setCreateDate((int)(System.currentTimeMillis() / 1000));
        log.setResponse(response);
        log.setCustomerId(message.getCustomerId());
        log.setMessageId(message.getId());
        log.setTopicId(message.getTopicId());
        log.setTime(time);
        this.messageLogMapper.createSuccessLog(log);

        // 修改消息状态
        this.messageMapper.success(id);
    }

    @Override
    public void messageFiled(Long id, String error, Integer time) {
        // 生成执行日志
        Message message = this.messageMapper.getById(id);
        MessageLog log = new MessageLog();
        log.setCreateDate((int)(System.currentTimeMillis() / 1000));
        log.setError(error);
        log.setCustomerId(message.getCustomerId());
        log.setMessageId(message.getId());
        log.setTopicId(message.getTopicId());
        log.setTime(time);
        this.messageLogMapper.createErrorLog(log);

        Customer customer = this.customerMapper.getById(message.getCustomerId());
        if (message.getAttempts() >= customer.getAttempts()) {
            // 如果
            this.messageMapper.failed(id);
        } else {
            this.messageMapper.retry(id);
        }
    }

    public void retryTimeoutMessage() {
        this.messageMapper.retryTimeoutMessage();
    }
}
