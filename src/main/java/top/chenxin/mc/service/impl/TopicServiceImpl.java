package top.chenxin.mc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.entity.Message;
import top.chenxin.mc.form.topic.CreateForm;
import top.chenxin.mc.form.topic.ListForm;
import top.chenxin.mc.form.topic.PushForm;
import top.chenxin.mc.mapper.CustomerMapper;
import top.chenxin.mc.mapper.MessageMapper;
import top.chenxin.mc.mapper.TopicMapper;
import top.chenxin.mc.response.topic.ListResponse;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.service.TopicService;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void create(CreateForm createForm) {
        if (createForm.getName().length() == 0) {
            throw new RuntimeException("名称不能为空");
        }
        Topic topic = topicMapper.getByName(createForm.getName());
        if (topic != null) {
            throw new RuntimeException("Topic 已存在，请使用其他名称");
        }
        topicMapper.create(createForm.toTopic());
    }

    @Override
    public ListResponse search(ListForm listForm) {

        List<Topic> topicList = topicMapper.search(listForm);

        ListResponse response = new ListResponse(topicList);
        response.setPage(listForm.getPage());
        response.setLimit(listForm.getLimit());
        response.setCount(topicMapper.searchCount(listForm));
        return response;
    }

    @Transactional
    @Override
    public void push(PushForm form) {
        Topic topic = topicMapper.getByName(form.getTopicName());
        if (topic == null) {
            throw new RuntimeException("未找到topic");
        }

        List<Customer> customers = customerMapper.getByTopicId(topic.getId());

        for (Customer item : customers) {
            Message msg = messageMapper.getByMessageIdAndCustomerId(form.getMessageId(), item.getId());
            if (msg != null) {
                throw new RuntimeException("消息已保存");
            }

            msg = new Message();
            msg.setMessageId(form.getMessageId());
            msg.setTopicId(topic.getId());
            msg.setCustomerId(item.getId());
            msg.setMessage(form.getMessage());
            msg.setAvailableDate(form.getAvailableDate());
            msg.setCreateDate((int) (System.currentTimeMillis() / 1000));
            messageMapper.create(msg);
        }
    }
}
