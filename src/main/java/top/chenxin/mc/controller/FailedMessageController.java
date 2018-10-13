package top.chenxin.mc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.chenxin.mc.request.failed_message.ListForm;
import top.chenxin.mc.service.MessageService;

import java.util.Map;

@RestController
@RequestMapping("")
public class FailedMessageController extends BaseController {

    @Autowired
    private MessageService messageService;

    /**
     * 查看失败的消息
     */
    @RequestMapping(value = "failed_message", method = RequestMethod.GET)
    public Map index(@Validated ListForm form) {
        return messageService.getFailedMessageList(form.getCustomerId(), form.getPage(), form.getLimit()).toMap();
    }

    /**
     * 删除失败的消息
     */
    @RequestMapping(value = "failed_message/{id}", method = RequestMethod.DELETE)
    private Map delete(@PathVariable("id") Long id) {
        messageService.deleteFailedMessage(id);
        return success();
    }

    /**
     * 重试
     */
    @RequestMapping(value = "failed_message/{id}/retry", method = RequestMethod.PUT)
    private Map retry(@PathVariable("id") Long id) {
        messageService.retryMessage(id);
        return success();
    }

}
