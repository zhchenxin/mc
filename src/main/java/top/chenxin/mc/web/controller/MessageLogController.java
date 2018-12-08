package top.chenxin.mc.web.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.chenxin.mc.service.model.PageList;
import top.chenxin.mc.web.APIResponse;
import top.chenxin.mc.web.request.message_log.ListForm;
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
    public ResponseEntity<JSONObject> index(@Validated ListForm form) {
        PageList pageList = messageService.getMessageLogPage(form.getCustomerId(), form.getMessageId(),form.getPage(), form.getLimit());
        return APIResponse.success(pageList);
    }
}
