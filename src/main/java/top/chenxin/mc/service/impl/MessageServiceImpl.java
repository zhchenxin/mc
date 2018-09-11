package top.chenxin.mc.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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

        Page<Message> messageList = messageMapper.search(listForm, listForm.getPage(), listForm.getLimit());
        if (messageList.size() == 0) {
            return new EmptyPaginationResponse(messageList);
        }

        List<Long> customerIds = messageList.stream().map(Message::getCustomerId).collect(Collectors.toList());
        List<Long> topicIds = messageList.stream().map(Message::getTopicId).collect(Collectors.toList());
        Utils.removeDuplicate(customerIds);
        Utils.removeDuplicate(topicIds);

        List<Customer> customerList = customerMapper.getByIds(customerIds);
        List<Topic> topicList = topicMapper.getByIds(topicIds);

        ListResponse response = new ListResponse(messageList, customerList, topicList);
        response.setPage(messageList);
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

}
