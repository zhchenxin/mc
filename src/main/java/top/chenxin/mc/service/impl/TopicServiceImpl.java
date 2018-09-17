package top.chenxin.mc.service.impl;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.chenxin.mc.common.constant.ErrorCode;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.entity.Message;
import top.chenxin.mc.dao.CustomerDao;
import top.chenxin.mc.dao.MessageDao;
import top.chenxin.mc.dao.TopicDao;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.service.TopicService;
import top.chenxin.mc.service.exception.ServiceException;

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
            throw new ServiceException("topic name 重复");
        }

        topic = new Topic();
        topic.setName(name);
        topic.setDescription(description);
        topicDao.insert(topic);
    }

    @Override
    public void update(Long id, String name, String description) {
        Topic topic = new Topic();
        topic.setId(id);
        topic.setName(name);
        topic.setDescription(description);
        topicDao.update(topic);
    }

    @Override
    public void delete(Long id) {
        topicDao.delete(id);
    }

    @Override
    public Page<Topic> search(Long topicId, Integer page, Integer limit) {
        return topicDao.search(topicId, page, limit);
    }

    @Override
    public List<Topic> getByIds(List<Long> ids) {
        return topicDao.getByIds(ids);
    }

    @Override
    public Topic getById(Long id) {
        return topicDao.getById(id);
    }

    public List<Topic> getAll() {
        return topicDao.getAll();
    }

    @Transactional
    @Override
    public void push(Long messageId, String topicName, String message, Integer delay) {
        Topic topic = topicDao.getByName(topicName);
        if (topic == null) {
            throw new ServiceException("topic 不存在", ErrorCode.OBJECT_NOT_DOUND);
        }
        this.push(messageId, topic, message, delay);
    }

    @Override
    public void push(Long messageId, Long topicId, String message, Integer delay) {
        Topic topic = topicDao.getById(topicId);
        if (topic == null) {
            throw new ServiceException("topic 不存在", ErrorCode.OBJECT_NOT_DOUND);
        }

        this.push(messageId, topic, message, delay);
    }

    @Transactional
    private void push(Long messageId, Topic topic, String message, Integer delay) {
        List<Customer> customers = customerDao.getByTopicId(topic.getId());

        for (Customer item : customers) {
            Message msg = messageDao.getByMessageIdAndCustomerId(messageId, item.getId());
            if (msg != null) {
                throw new ServiceException("message id 重复");
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
