package top.chenxin.mc.web.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.web.APIResponse;
import top.chenxin.mc.web.request.topic.CreateForm;
import top.chenxin.mc.web.request.topic.ListForm;
import top.chenxin.mc.web.request.topic.PushForm;
import top.chenxin.mc.web.request.topic.UpdateForm;
import top.chenxin.mc.service.TopicService;

import java.util.List;

@RestController
@RequestMapping("")
public class TopicController extends BaseController {

    @Autowired
    private TopicService topicService;

    @RequestMapping(value = "topic", method = RequestMethod.GET)
    private ResponseEntity<JSONObject> index(@Validated ListForm form) {
        List<Long> ids = Utils.getIds(form.getId());
        return APIResponse.success(topicService.getPage(ids, form.getPage(), form.getLimit()));
    }

    @RequestMapping(value = "topic", method = RequestMethod.POST)
    private ResponseEntity<JSONObject> create(@Validated CreateForm form) {
        topicService.insert(form.getName(), form.getDescription());
        return APIResponse.success();
    }

    @RequestMapping(value = "topic/{id}", method = RequestMethod.PUT)
    private ResponseEntity<JSONObject> update(@PathVariable("id") Long id, @Validated UpdateForm form) {
        topicService.update(id, form.getName(), form.getDescription());
        return APIResponse.success();
    }

    @RequestMapping(value = "topic/{id}", method = RequestMethod.DELETE)
    private ResponseEntity<JSONObject> delete(@PathVariable("id") Long id) {
        topicService.delete(id);
        return APIResponse.success();
    }

    @RequestMapping(value = "topic/{name}/push", method = RequestMethod.POST)
    private ResponseEntity<JSONObject> push(@PathVariable("name") String name, @Validated PushForm form) {
        topicService.push(name, form.getMessage(), form.getDelay());
        return APIResponse.success();
    }
}
