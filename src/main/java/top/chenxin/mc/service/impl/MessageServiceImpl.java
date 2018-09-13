package top.chenxin.mc.service.impl;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.chenxin.mc.common.constant.ErrorCode;
import top.chenxin.mc.dao.po.Topic;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.service.dto.MessageDetail;
import top.chenxin.mc.service.dto.MessageSearchList;
import top.chenxin.mc.dao.CustomerDao;
import top.chenxin.mc.dao.MessageDao;
import top.chenxin.mc.dao.MessageLogDao;
import top.chenxin.mc.dao.TopicDao;
import top.chenxin.mc.dao.po.Customer;
import top.chenxin.mc.dao.po.Message;
import top.chenxin.mc.dao.po.MessageLog;
import top.chenxin.mc.service.MessageService;
import top.chenxin.mc.service.exception.ServiceException;

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
    public MessageSearchList search(Long customerId, Integer status, Integer page, Integer limit) {

        Page<Message> messageList = messageDao.search(customerId, status, page, limit);
        if (messageList.size() == 0) {
            return new MessageSearchList(messageList, null, null);
        }

        List<Long> customerIds = messageList.stream().map(Message::getCustomerId).collect(Collectors.toList());
        List<Long> topicIds = messageList.stream().map(Message::getTopicId).collect(Collectors.toList());
        Utils.removeDuplicate(customerIds);
        Utils.removeDuplicate(topicIds);

        List<Customer> customerList = customerDao.getByIds(customerIds);
        List<Topic> topicList = topicDao.getByIds(topicIds);

        return new MessageSearchList(messageList, customerList, topicList);
    }

    @Override
    public MessageDetail getDetailById(Long id) {
        Message message = messageDao.getById(id);
        if (message == null) {
            throw new ServiceException("Not Found", ErrorCode.OBJECT_NOT_DOUND);
        }

        Customer customer = customerDao.getById(message.getCustomerId());
        Topic topic = topicDao.getById(message.getTopicId());
        List<MessageLog> logs = messageLogDao.getByMessageId(message.getId());
        return new MessageDetail(message, topic, customer, logs);
    }

}
