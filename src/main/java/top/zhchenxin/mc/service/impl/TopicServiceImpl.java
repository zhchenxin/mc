package top.zhchenxin.mc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zhchenxin.mc.mapper.CustomerMapper;
import top.zhchenxin.mc.mapper.MessageMapper;
import top.zhchenxin.mc.mapper.TopicMapper;
import top.zhchenxin.mc.entity.Customer;
import top.zhchenxin.mc.entity.Message;
import top.zhchenxin.mc.entity.Topic;
import top.zhchenxin.mc.form.topic.CreateForm;
import top.zhchenxin.mc.form.topic.ListForm;
import top.zhchenxin.mc.form.topic.PushForm;
import top.zhchenxin.mc.resource.TopicCollection;
import top.zhchenxin.mc.service.TopicService;

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
    public TopicCollection search(ListForm listForm) {
        TopicCollection collection = new TopicCollection();
        collection.setPage(listForm.getPage());
        collection.setLimit(listForm.getLimit());
        collection.setCount(topicMapper.searchCount(listForm));
        collection.setList(topicMapper.search(listForm));
        return collection;
    }

    @Transactional
    @Override
    public void push(PushForm form) {
        Topic topic = this.topicMapper.getByName(form.getTopicName());
        if (topic == null) {
            throw new RuntimeException("未找到topic");
        }

        List<Customer> customers = this.customerMapper.getByTopic(topic.getId());

        for (int i = 0; i < customers.size(); i++) {
            Customer item = customers.get(i);
            Message msg = this.messageMapper.getByMessageIdAndCustomerId(form.getMessageId(), item.getId());
            if (msg != null) {
                throw new RuntimeException("消息已保存");
            }

            msg = new Message();
            msg.setMessageId(form.getMessageId());
            msg.setTopicId(topic.getId());
            msg.setCustomerId(item.getId());
            msg.setMessage(form.getMessage());
            msg.setAvailableDate(form.getAvailableDate());
            msg.setCreateDate((int)(System.currentTimeMillis() / 1000));
            this.messageMapper.create(msg);
        }
    }
}
