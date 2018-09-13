package top.chenxin.mc.service.impl;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.dao.po.Customer;
import top.chenxin.mc.dao.po.Message;
import top.chenxin.mc.dao.CustomerDao;
import top.chenxin.mc.dao.MessageDao;
import top.chenxin.mc.dao.TopicDao;
import top.chenxin.mc.dao.po.Topic;
import top.chenxin.mc.service.TopicService;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private MessageDao messageDao;

    @Override
    public void insert(String name, String description) {
        Topic topic = topicDao.getByName(name);
        if (topic != null) {
            throw new RuntimeException("Topic 已存在，请使用其他名称");
        }

        topic = new Topic();
        topic.setName(name);
        topic.setDescription(description);
        topicDao.insert(topic);
    }

    @Override
    public Page<Topic> search(Long topicId, Integer page, Integer limit) {
        return topicDao.search(topicId, page, limit);
    }

    @Transactional
    @Override
    public void push(Long messageId, String topicName, String message, Integer delay) {
        Topic topic = topicDao.getByName(topicName);
        if (topic == null) {
            throw new RuntimeException("未找到topic");
        }

        List<Customer> customers = customerDao.getByTopicId(topic.getId());

        for (Customer item : customers) {
            Message msg = messageDao.getByMessageIdAndCustomerId(messageId, item.getId());
            if (msg != null) {
                throw new RuntimeException("消息已保存");
            }

            msg = new Message();
            msg.setMessageId(messageId);
            msg.setTopicId(topic.getId());
            msg.setCustomerId(item.getId());
            msg.setMessage(message);
            msg.setAvailableDate(Utils.getCurrentTimestamp() + delay);
            msg.setCreateDate(Utils.getCurrentTimestamp());
            messageDao.insert(msg);
        }
    }
}
