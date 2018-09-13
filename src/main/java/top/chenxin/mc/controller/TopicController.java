package top.chenxin.mc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.chenxin.mc.request.topic.CreateForm;
import top.chenxin.mc.request.topic.ListForm;
import top.chenxin.mc.request.topic.PushForm;
import top.chenxin.mc.response.PaginationResponse;
import top.chenxin.mc.response.SuccessResponse;
import top.chenxin.mc.service.TopicService;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/topic")
public class TopicController extends BaseController {
    @Autowired
    private TopicService topicService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private Map list(@Validated ListForm form) {
        PaginationResponse collection = topicService.search(form);
        return collection.toMap();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    private Map create(@Validated CreateForm form) {
        topicService.create(form);
        return new SuccessResponse().toMap();
    }

    @RequestMapping(value = "/push", method = RequestMethod.POST)
    private Map push(@Validated PushForm form) {
        if (form.getMessageId() == 0) {
            Random random = new Random();
            form.setMessageId(random.nextLong());
        }
        topicService.push(form);
        return new SuccessResponse().toMap();
    }
}
