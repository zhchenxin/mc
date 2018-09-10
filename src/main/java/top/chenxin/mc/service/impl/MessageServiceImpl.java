package top.chenxin.mc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.chenxin.mc.lib.Utils;
import top.chenxin.mc.resource.MessageCollection;
import top.chenxin.mc.mapper.CustomerMapper;
import top.chenxin.mc.mapper.MessageMapper;
import top.chenxin.mc.mapper.MessageLogMapper;
import top.chenxin.mc.mapper.TopicMapper;
import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.entity.Message;
import top.chenxin.mc.entity.MessageLog;
import top.chenxin.mc.form.message.ListForm;
import top.chenxin.mc.resource.MessageDetail;
import top.chenxin.mc.service.MessageService;

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
        collection.setCount(messageMapper.searchCount(listForm));
        collection.setList(messageMapper.search(listForm));

        if (collection.getList().size() == 0) {
            return collection;
        }

        List<Long> customerIds = collection.getList().stream().map(Message::getCustomerId).collect(Collectors.toList());
        List<Long> topicIds = collection.getList().stream().map(Message::getTopicId).collect(Collectors.toList());
        Utils.removeDuplicate(customerIds);
        Utils.removeDuplicate(topicIds);

        collection.setCustomerList(customerMapper.getByIds(customerIds));
        collection.setTopicList(topicMapper.getByIds(topicIds));

        return collection;
    }

    @Override
    public MessageDetail getDetailById(Long id) {
        MessageDetail detail = new MessageDetail();
        Message message = messageMapper.getById(id);
        if (message == null) {
            return null;
        }

        detail.setEntity(message);
        detail.setCustomer(customerMapper.getById(message.getCustomerId()));
        detail.setTopic(topicMapper.getById(message.getTopicId()));
        detail.setLogList(messageLogMapper.getByMessageId(message.getId()));
        return detail;
    }

    @Override
    @Transactional
    public MessageDetail pop() {
        Message message = messageMapper.popMessage();
        if (message == null) {
            return null;
        }
        Customer customer = customerMapper.getById(message.getCustomerId());
        messageMapper.start(message.getId(), Utils.getCurrentTimestamp() + customer.getTimeout());
        return getDetailById(message.getId());
    }

    @Override
    @Transactional
    public void messageSuccess(Long id, String response, Integer time) {
        // 生成执行日志
        Message message = messageMapper.getById(id);
        MessageLog log = new MessageLog();
        log.setCreateDate((int)(System.currentTimeMillis() / 1000));
        log.setResponse(response);
        log.setCustomerId(message.getCustomerId());
        log.setMessageId(message.getId());
        log.setTopicId(message.getTopicId());
        log.setTime(time);
        log.setError("");
        messageLogMapper.create(log);

        // 修改消息状态
        messageMapper.success(id);
    }

    @Override
    public void messageFiled(Long id, String error, Integer time) {
        // 生成执行日志
        Message message = messageMapper.getById(id);
        MessageLog log = new MessageLog();
        log.setCreateDate((int)(System.currentTimeMillis() / 1000));
        log.setError(error);
        log.setCustomerId(message.getCustomerId());
        log.setMessageId(message.getId());
        log.setTopicId(message.getTopicId());
        log.setTime(time);
        log.setResponse("");
        messageLogMapper.create(log);

        Customer customer = customerMapper.getById(message.getCustomerId());
        if (message.getAttempts() >= customer.getAttempts()) {
            // 如果
            messageMapper.failed(id);
        } else {
            messageMapper.retry(id);
        }
    }

    public void retryTimeoutMessage() {
        messageMapper.retryTimeoutMessage();
    }
}
