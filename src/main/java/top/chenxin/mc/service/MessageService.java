package top.chenxin.mc.service;

import top.chenxin.mc.entity.Message;
import top.chenxin.mc.response.Response;
import top.chenxin.mc.response.message.ListResponse;
import top.chenxin.mc.form.message.ListForm;
import top.chenxin.mc.response.message.DetailResponse;

public interface MessageService {
    Response search(ListForm listForm);

    Response getDetailById(Long id);

    // 从消息中推出一条消息
    Message pop();

    // 消息执行成功
    void messageSuccess(Long id, String response, Integer time);

    // 消息执行失败
    // 消息执行失败之后, 如果重试次数小于消费者设置的次数, 则消息会重试
    void messageFiled(Long id, String error, Integer time);

    // 重试执行超时消息
    // 程序异常中断后, 导致一些正在执行的消息状态错误, 此方法用于修复这个错误
    void retryTimeoutMessage();
}
