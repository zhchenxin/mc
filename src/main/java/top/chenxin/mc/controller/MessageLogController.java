package top.chenxin.mc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.chenxin.mc.request.message_log.ListForm;
import top.chenxin.mc.service.MessageService;

import java.util.Map;

@RestController
@RequestMapping("")
public class MessageLogController extends BaseController {
    @Autowired
    private MessageService messageService;

    /**
     * 查看执行日志
     */
    @RequestMapping(value = "message_log", method = RequestMethod.GET)
    public Map index(@Validated ListForm form) {
        return messageService.getMessageLogList(form.getCustomerId(), form.getMessageId(),form.getPage(), form.getLimit()).toMap();
    }
}
