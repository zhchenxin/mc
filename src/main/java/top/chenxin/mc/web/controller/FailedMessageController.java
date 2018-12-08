package top.chenxin.mc.web.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.chenxin.mc.web.APIResponse;
import top.chenxin.mc.web.request.failed_message.ListForm;
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
    public ResponseEntity<JSONObject> index(@Validated ListForm form) {
        return APIResponse.success(messageService.getFailedMessageList(form.getCustomerId(), form.getPage(), form.getLimit()));
    }

    /**
     * 删除失败的消息
     */
    @RequestMapping(value = "failed_message/{id}", method = RequestMethod.DELETE)
    private ResponseEntity<JSONObject> delete(@PathVariable("id") Long id) {
        messageService.deleteFailedMessage(id);
        return APIResponse.success();
    }

    /**
     * 重试
     */
    @RequestMapping(value = "failed_message/{id}/retry", method = RequestMethod.PUT)
    private ResponseEntity<JSONObject> retry(@PathVariable("id") Long id) {
        messageService.retryMessage(id);
        return APIResponse.success();
    }

}
