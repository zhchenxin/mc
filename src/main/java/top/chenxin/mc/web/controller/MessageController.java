package top.chenxin.mc.web.controller;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.entity.Message;
import top.chenxin.mc.entity.MessageLog;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.service.CustomerService;
import top.chenxin.mc.web.request.message.ListForm;
import top.chenxin.mc.service.MessageService;
import top.chenxin.mc.service.TopicService;
import top.chenxin.mc.web.response.message.DetailResponse;
import top.chenxin.mc.web.response.message.ListResponse;

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

    @RequestMapping(value = "",method = RequestMethod.GET)
    public Map index(@Validated ListForm form) {
        Page<Message> messageList = messageService.search(form.getCustomerId(), form.getStatus(), form.getPage(), form.getLimit());

        if (messageList.isEmpty()) {
            return new ListResponse(messageList, null, null).toMap();
        }

        List<Long> customerIds = messageList.stream().map(Message::getCustomerId).collect(Collectors.toList());
        List<Long> topicIds = messageList.stream().map(Message::getTopicId).collect(Collectors.toList());
        Utils.removeDuplicate(customerIds);
        Utils.removeDuplicate(topicIds);

        List<Customer> customerList = customerService.getByIds(customerIds);
        List<Topic> topicList = topicService.getByIds(topicIds);

        return new ListResponse(messageList, customerList, topicList).toMap();
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public Map detail(@RequestParam("id") Long id) {
        Message message = messageService.getById(id);
        Topic topic = topicService.getById(message.getTopicId());
        Customer customer = customerService.getById(message.getCustomerId());
        List<MessageLog> messageLogList = messageService.getMessageLogs(message.getId());
        return new DetailResponse(message, topic, customer, messageLogList).toMap();
    }

}
