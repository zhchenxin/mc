package top.chenxin.mc.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.chenxin.mc.service.dto.MessageDetail;
import top.chenxin.mc.service.dto.MessageSearchList;
import top.chenxin.mc.web.request.message.ListForm;
import top.chenxin.mc.service.MessageService;
import top.chenxin.mc.service.TopicService;
import top.chenxin.mc.web.response.message.DetailResponse;
import top.chenxin.mc.web.response.message.ListResponse;

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
        MessageSearchList messageSearchList = messageService.search(form.getCustomerId(), form.getStatus(), form.getPage(), form.getLimit());
        return new ListResponse(messageSearchList).toMap();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Map detail(@PathVariable("id") Long id) {
        MessageDetail messageDetail = messageService.getDetailById(id);
        return new DetailResponse(messageDetail).toMap();
    }

}
