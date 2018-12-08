package top.chenxin.mc.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.web.request.topic.CreateForm;
import top.chenxin.mc.web.request.topic.ListForm;
import top.chenxin.mc.web.request.topic.PushForm;
import top.chenxin.mc.web.request.topic.UpdateForm;
import top.chenxin.mc.service.TopicService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("")
public class TopicController extends BaseController {

    @Autowired
    private TopicService topicService;

    @RequestMapping(value = "topic", method = RequestMethod.GET)
    private Map index(@Validated ListForm form) {
        List<Long> ids = Utils.getIds(form.getId());
        return topicService.getList(ids, form.getPage(), form.getLimit()).toMap();
    }

    @RequestMapping(value = "topic", method = RequestMethod.POST)
    private Map create(@Validated CreateForm form) {
        topicService.insert(form.getName(), form.getDescription());
        return success();
    }

    @RequestMapping(value = "topic/{id}", method = RequestMethod.PUT)
    private Map update(@PathVariable("id") Long id, @Validated UpdateForm form) {
        topicService.update(id, form.getName(), form.getDescription());
        return success();
    }

    @RequestMapping(value = "topic/{id}", method = RequestMethod.DELETE)
    private Map delete(@PathVariable("id") Long id) {
        topicService.delete(id);
        return success();
    }

    @RequestMapping(value = "topic/{name}/push", method = RequestMethod.POST)
    private Map push(@PathVariable("name") String name, @Validated PushForm form) {
        topicService.push(name, form.getMessage(), form.getDelay());
        return success();
    }
}
