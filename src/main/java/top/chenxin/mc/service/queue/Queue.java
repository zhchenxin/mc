package top.chenxin.mc.service.queue;

import top.chenxin.mc.model.MessageModel;

public interface Queue {

    // 从队列中弹出消息，如果没有消息，则返回null
    MessageModel pop();

    // 相队列中添加一条消息
    void push(MessageModel message);

    // 将超时的订单，重新放入队列中
    void migrate();

    // 消息执行成功时调用
    void success(MessageModel message);

    // 是否需要重试
    default boolean needRetry(MessageModel message) {
        return message.getAttempts() < message.getMaxAttempts();
    }

    // 消息执行失败时调用
    void failed(MessageModel message);
}
