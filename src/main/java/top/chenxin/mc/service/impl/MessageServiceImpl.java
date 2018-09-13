package top.chenxin.mc.service.impl;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.lib.Utils;
import top.chenxin.mc.response.EmptyPaginationResponse;
import top.chenxin.mc.response.Response;
import top.chenxin.mc.response.message.ListResponse;
import top.chenxin.mc.dao.CustomerDao;
import top.chenxin.mc.dao.MessageDao;
import top.chenxin.mc.dao.MessageLogDao;
import top.chenxin.mc.dao.TopicDao;
import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.entity.Message;
import top.chenxin.mc.entity.MessageLog;
import top.chenxin.mc.request.message.ListForm;
import top.chenxin.mc.response.message.DetailResponse;
import top.chenxin.mc.service.MessageService;

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
    public Response search(ListForm listForm) {

        Page<Message> messageList = messageDao.search(listForm, listForm.getPage(), listForm.getLimit());
        if (messageList.size() == 0) {
            return new EmptyPaginationResponse(messageList);
        }

        List<Long> customerIds = messageList.stream().map(Message::getCustomerId).collect(Collectors.toList());
        List<Long> topicIds = messageList.stream().map(Message::getTopicId).collect(Collectors.toList());
        Utils.removeDuplicate(customerIds);
        Utils.removeDuplicate(topicIds);

        List<Customer> customerList = customerDao.getByIds(customerIds);
        List<Topic> topicList = topicDao.getByIds(topicIds);

        ListResponse response = new ListResponse(messageList, customerList, topicList);
        response.setPage(messageList);
        return response;
    }

    @Override
    public Response getDetailById(Long id) {

        Message message = messageDao.getById(id);
        if (message == null) {
            throw new RuntimeException("Not Found");
        }

        Customer customer = customerDao.getById(message.getCustomerId());
        Topic topic = topicDao.getById(message.getTopicId());
        List<MessageLog> logs = messageLogDao.getByMessageId(message.getId());
        return new DetailResponse(message, topic, customer, logs);
    }

}
