package top.chenxin.mc.web.controller;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.entity.*;
import top.chenxin.mc.service.CustomerService;
import top.chenxin.mc.service.MessageService;
import top.chenxin.mc.service.TopicService;
import top.chenxin.mc.web.request.message.LogForm;
import top.chenxin.mc.web.response.SuccessResponse;
import top.chenxin.mc.web.response.message.FailedResponse;
import top.chenxin.mc.web.response.message.LogResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
public class MessageController extends BaseController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private CustomerService customerService;

    /**
     * 消息日志
     */
    @RequestMapping(value = "log", method = RequestMethod.GET)
    public Map log(@Validated LogForm form) {

        Page<MessageLog> messageList = messageService.searchLog(form.getCustomerId(), form.getPage(), form.getLimit());

        if (messageList.isEmpty()) {
            return new LogResponse(messageList, null, null).toMap();
        }

        List<Long> customerIds = messageList.stream().map(MessageLog::getCustomerId).collect(Collectors.toList());
        List<Long> topicIds = messageList.stream().map(MessageLog::getTopicId).collect(Collectors.toList());
        Utils.removeDuplicate(customerIds);
        Utils.removeDuplicate(topicIds);

        List<Customer> customerList = customerService.getByIds(customerIds);
        List<Topic> topicList = topicService.getByIds(topicIds);

        return new LogResponse(messageList, customerList, topicList).toMap();
    }

    /**
     * 失败的消息
     */
    @RequestMapping(value = "failed", method = RequestMethod.GET)
    public Map failed(@Validated LogForm form) {
        Page<FailedMessage> messageList = messageService.searchFailed(form.getCustomerId(), form.getPage(), form.getLimit());

        if (messageList.isEmpty()) {
            return new FailedResponse(messageList, null, null).toMap();
        }

        List<Long> customerIds = messageList.stream().map(FailedMessage::getCustomerId).collect(Collectors.toList());
        List<Long> topicIds = messageList.stream().map(FailedMessage::getTopicId).collect(Collectors.toList());
        Utils.removeDuplicate(customerIds);
        Utils.removeDuplicate(topicIds);

        List<Customer> customerList = customerService.getByIds(customerIds);
        List<Topic> topicList = topicService.getByIds(topicIds);

        return new FailedResponse(messageList, customerList, topicList).toMap();
    }

    /**
     * 重试失败消息
     */
    @RequestMapping(value = "retry", method = RequestMethod.POST)
    public Map retry(@RequestParam("messageId") Long messageId) {
        messageService.retryMessage(messageId);
        return new SuccessResponse().toMap();
    }

    /**
     * 删除失败消息
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map delete(@RequestParam("messageId") Long messageId) {
        messageService.deleteFailedMessage(messageId);
        return new SuccessResponse().toMap();
    }
}
