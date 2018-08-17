package top.zhchenxin.mc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.zhchenxin.mc.form.topic.CreateForm;
import top.zhchenxin.mc.form.topic.ListForm;
import top.zhchenxin.mc.form.topic.PushForm;
import top.zhchenxin.mc.lib.PaginationCollection;
import top.zhchenxin.mc.lib.BaseController;
import top.zhchenxin.mc.service.TopicService;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/topic")
public class TopicController extends BaseController {
    @Autowired
    private TopicService topicService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private Map<String, Object> list(ListForm form) {
        PaginationCollection collection = topicService.search(form);
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
