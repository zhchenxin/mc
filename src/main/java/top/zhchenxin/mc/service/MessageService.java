package top.zhchenxin.mc.service;

import top.zhchenxin.mc.entity.Message;
import top.zhchenxin.mc.form.message.ListForm;
import top.zhchenxin.mc.resource.MessageCollection;
import top.zhchenxin.mc.resource.MessageDetail;

public interface MessageService {
    MessageCollection search(ListForm listForm);

    MessageDetail getDetailById(Long id);

    // 从消息中推出一条消息
    Message pop();

    // 消息执行成功
    void messageSuccess(Long id, String response, Integer time);

    // 消息执行失败
    void messageFiled(Long id, String error, Integer time);

    // 重试执行超时消息
    void retryTimeoutMessage();
}
