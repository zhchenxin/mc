package top.chenxin.mc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.chenxin.mc.request.topic.CreateForm;
import top.chenxin.mc.request.topic.ListForm;
import top.chenxin.mc.request.topic.PushForm;
import top.chenxin.mc.response.PaginationResponse;
import top.chenxin.mc.lib.BaseController;
import top.chenxin.mc.service.TopicService;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/topic")
public class TopicController extends BaseController {
    @Autowired
    private TopicService topicService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private Map<String, Object> list(ListForm form) {
        PaginationResponse collection = topicService.search(form);
        return successJson(collection.toMap());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    private Map<String, Object> create(CreateForm form) {
        topicService.create(form);
        return successJson(null);
    }

    @RequestMapping(value = "/push", method = RequestMethod.POST)
    private Map<String, Object> push(PushForm form) {
        if (form.getMessageId() == 0) {
            Random random = new Random();
            form.setMessageId(random.nextLong());
        }
        topicService.push(form);
        return successJson(null);
    }
}
