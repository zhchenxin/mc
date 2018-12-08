package top.chenxin.mc.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.chenxin.mc.common.constant.ErrorCode;
import top.chenxin.mc.common.utils.IdBuilder;
import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.dao.CustomerDao;
import top.chenxin.mc.dao.TopicDao;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.service.model.MessageModel;
import top.chenxin.mc.service.TopicService;
import top.chenxin.mc.service.exception.ServiceException;
import top.chenxin.mc.service.model.PageList;
import top.chenxin.mc.service.queue.Queue;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private Queue queue;

    @Autowired
    private IdBuilder idBuilder;

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
        Topic topic = topicDao.getByName(name);
        if (topic != null && !topic.getId().equals(id)) {
            throw new ServiceException("topic name 重复");
        }

        topic = new Topic();
        topic.setId(id);
        topic.setName(name);
        topic.setDescription(description);
        topicDao.update(topic);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        topicDao.delete(id);

        // 删除对应的消费者
        List<Customer> customers = customerDao.getByTopicId(id);
        for (Customer customer : customers) {
            customerDao.delete(customer.getId());
        }
    }

    @Override
    public PageList<Topic> getPage(List<Long> ids, Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        Page<Topic> topics = (Page<Topic>) this.topicDao.getList(ids);
        return new PageList<>(topics);
    }

    @Transactional
    @Override
    public void push(String topicName, String message, Integer delay) {
        Topic topic = topicDao.getByName(topicName);
        if (topic == null) {
            throw new ServiceException("topic 不存在", ErrorCode.OBJECT_NOT_DOUND);
        }
        this.push(topic, message, delay);
    }

    @Transactional
    @Override
    public void push(Long topicId, String message, Integer delay) {
        Topic topic = topicDao.getById(topicId);
        if (topic == null) {
            throw new ServiceException("topic 不存在", ErrorCode.OBJECT_NOT_DOUND);
        }

        this.push(topic, message, delay);
    }

    private void push(Topic topic, String message, Integer delay) {
        List<Customer> customers = customerDao.getByTopicId(topic.getId());

        for (Customer item : customers) {
            MessageModel msg = new MessageModel();
            msg.setId(idBuilder.createMessageId());
            msg.setTopicId(topic.getId());
            msg.setCustomerId(item.getId());
            msg.setMessage(message);
            msg.setAttempts(0);
            msg.setMaxAttempts(item.getAttempts());
            msg.setDelay(delay);
            msg.setTimeout(item.getTimeout());
            queue.push(msg);
        }
    }
}
