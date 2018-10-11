package top.chenxin.mc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.chenxin.mc.request.topic.CreateForm;
import top.chenxin.mc.request.topic.ListForm;
import top.chenxin.mc.request.topic.PushForm;
import top.chenxin.mc.request.topic.UpdateForm;
import top.chenxin.mc.service.TopicService;

import java.util.Map;

@RestController
@RequestMapping("/topic")
public class TopicController extends BaseController {

    @Autowired
    private TopicService topicService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private Map index(@Validated ListForm form) {
        return topicService.getList(null, form.getPage(), form.getLimit()).toMap();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    private Map create(@Validated CreateForm form) {
        topicService.insert(form.getName(), form.getDescription());
        return success();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    private Map update(@PathVariable("id") Long id, @Validated UpdateForm form) {
        topicService.update(id, form.getName(), form.getDescription());
        return success();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    private Map delete(@PathVariable("id") Long id) {
        topicService.delete(id);
        return success();
    }

    @RequestMapping(value = "/{name}/push", method = RequestMethod.POST)
    private Map push(@PathVariable("name") String name, @Validated PushForm form) {
        topicService.push(name, form.getMessage(), form.getDelay());
        return success();
    }
}
