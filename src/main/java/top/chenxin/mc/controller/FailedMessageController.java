package top.chenxin.mc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.chenxin.mc.request.customer.ListForm;
import top.chenxin.mc.service.MessageService;

import java.util.Map;

@RestController
@RequestMapping("failed_message")
public class FailedMessageController extends BaseController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map index(@Validated ListForm form) {
        return messageService.getFailedMessageList(form.getTopicId(), form.getPage(), form.getLimit()).toMap();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    private Map delete(@PathVariable("id") Long id) {
        messageService.deleteFailedMessage(id);
        return success();
    }

    @RequestMapping(value = "{id}/retry", method = RequestMethod.PUT)
    private Map retry(@PathVariable("id") Long id) {
        messageService.retryMessage(id);
        return success();
    }

}
