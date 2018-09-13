package top.chenxin.mc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.chenxin.mc.request.message.ListForm;
import top.chenxin.mc.lib.BaseController;
import top.chenxin.mc.service.MessageService;
import top.chenxin.mc.service.TopicService;

import java.util.Map;

@RestController
@RequestMapping("/message")
public class MessageController extends BaseController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private TopicService topicService;

    @RequestMapping(value = "",method = RequestMethod.GET)
    public Map list(@Validated ListForm form) {
        return messageService.search(form).toMap();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Map detail(@PathVariable("id") Long id) {
        return messageService.getDetailById(id).toMap();
    }

}
