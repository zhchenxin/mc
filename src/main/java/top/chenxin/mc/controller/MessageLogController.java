package top.chenxin.mc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.chenxin.mc.request.customer.ListForm;
import top.chenxin.mc.service.MessageService;

import java.util.Map;

@RestController
@RequestMapping("message_log")
public class MessageLogController extends BaseController {
    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map index(@Validated ListForm form) {
        return messageService.getMessageLogList(form.getTopicId(), form.getPage(), form.getLimit()).toMap();
    }
}
