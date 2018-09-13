package top.chenxin.mc.service.impl;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.chenxin.mc.dao.CustomerDao;
import top.chenxin.mc.dao.MessageDao;
import top.chenxin.mc.dao.MessageLogDao;
import top.chenxin.mc.dao.TopicDao;
import top.chenxin.mc.entity.Message;
import top.chenxin.mc.entity.MessageLog;
import top.chenxin.mc.service.MessageService;

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
}
