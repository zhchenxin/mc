package top.chenxin.mc.web.controller;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.web.request.topic.CreateForm;
import top.chenxin.mc.web.request.topic.ListForm;
import top.chenxin.mc.web.request.topic.PushForm;
import top.chenxin.mc.web.request.topic.UpdateForm;
import top.chenxin.mc.web.response.SuccessResponse;
import top.chenxin.mc.service.TopicService;
import top.chenxin.mc.web.response.topic.ListResponse;
import top.chenxin.mc.web.response.topic.UpdateResponse;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/topic")
public class TopicController extends BaseController {
    @Autowired
    private TopicService topicService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private Map list(@Validated ListForm form) {
        Page<Topic> topics = topicService.search(form.getTopicId(), form.getPage(), form.getLimit());
        return new ListResponse(topics).toMap();
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    private Map create(@Validated CreateForm form) {
        topicService.insert(form.getName(), form.getDescription());
        return new SuccessResponse().toMap();
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    private Map update(@PathVariable("id") Long id) {
        Topic topic = topicService.getById(id);
        return new UpdateResponse(topic).toMap();
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.POST)
    private Map update(@PathVariable("id") Long id, @Validated UpdateForm form) {
        topicService.update(id, form.getName(), form.getDescription());
        return new SuccessResponse().toMap();
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
    private Map delete(@PathVariable("id") Long id) {
        topicService.delete(id);
        return new SuccessResponse().toMap();
    }

    @RequestMapping(value = "/push", method = RequestMethod.POST)
    private Map push(@Validated PushForm form) {
        if (form.getMessageId() == 0) {
            Random random = new Random();
            form.setMessageId(random.nextLong());
        }
        topicService.push(form.getMessageId(), form.getTopicName(), form.getMessage(), form.getDelay());
        return new SuccessResponse().toMap();
    }
}
