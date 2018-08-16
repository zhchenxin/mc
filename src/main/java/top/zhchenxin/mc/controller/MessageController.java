package top.zhchenxin.mc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.zhchenxin.mc.form.message.ListForm;
import top.zhchenxin.mc.lib.PaginationCollection;
import top.zhchenxin.mc.lib.BaseController;
import top.zhchenxin.mc.service.MessageService;
import top.zhchenxin.mc.service.TopicService;

import java.util.Map;

@RestController
@RequestMapping("/message")
public class MessageController extends BaseController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private TopicService topicService;

    @RequestMapping(value = "",method = RequestMethod.GET)
    public Map<String,Object> list(ListForm form) {
        PaginationCollection collection = this.messageService.search(form);
        return this.successJson(collection.toMap());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Map<String, Object> detail(@PathVariable("id") Long id) {
        return this.successJson(this.messageService.getDetailById(id));
    }

}
