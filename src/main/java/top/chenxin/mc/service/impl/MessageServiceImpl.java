package top.chenxin.mc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.lib.Utils;
import top.chenxin.mc.response.EmptyPaginationResponse;
import top.chenxin.mc.response.Response;
import top.chenxin.mc.response.message.ListResponse;
import top.chenxin.mc.mapper.CustomerMapper;
import top.chenxin.mc.mapper.MessageMapper;
import top.chenxin.mc.mapper.MessageLogMapper;
import top.chenxin.mc.mapper.TopicMapper;
import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.entity.Message;
import top.chenxin.mc.entity.MessageLog;
import top.chenxin.mc.form.message.ListForm;
import top.chenxin.mc.response.message.DetailResponse;
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
    public Response search(ListForm listForm) {

        List<Message> messageList = messageMapper.search(listForm);
        if (messageList.size() == 0) {
            return new EmptyPaginationResponse();
        }

        List<Long> customerIds = messageList.stream().map(Message::getCustomerId).collect(Collectors.toList());
        List<Long> topicIds = messageList.stream().map(Message::getTopicId).collect(Collectors.toList());
        Utils.removeDuplicate(customerIds);
        Utils.removeDuplicate(topicIds);

        List<Customer> customerList = customerMapper.getByIds(customerIds);
        List<Topic> topicList = topicMapper.getByIds(topicIds);

        ListResponse response = new ListResponse(messageList, customerList, topicList);
        response.setPage(listForm.getPage());
        response.setLimit(listForm.getLimit());
        response.setCount(messageMapper.searchCount(listForm));
        return response;
    }

    @Override
    public Response getDetailById(Long id) {

        Message message = messageMapper.getById(id);
        if (message == null) {
            throw new RuntimeException("Not Found");
        }

        Customer customer = customerMapper.getById(message.getCustomerId());
        Topic topic =topicMapper.getById(message.getTopicId());
        List<MessageLog> logs = messageLogMapper.getByMessageId(message.getId());
        return new DetailResponse(message, topic, customer, logs);
    }

    @Override
    @Transactional
    public Message pop() {
        Message message = messageMapper.popMessage();
        if (message == null) {
            return null;
        }
        Customer customer = customerMapper.getById(message.getCustomerId());
        messageMapper.start(message.getId(), Utils.getCurrentTimestamp() + customer.getTimeout());
        return message;
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
