package top.chenxin.mc.console;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.entity.Message;
import top.chenxin.mc.entity.MessageLog;
import top.chenxin.mc.lib.Utils;
import top.chenxin.mc.mapper.CustomerMapper;
import top.chenxin.mc.mapper.MessageLogMapper;
import top.chenxin.mc.mapper.MessageMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Component
@Configuration
public class CustomerJob implements Runnable, InitializingBean, DisposableBean {


    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private MessageLogMapper messageLogMapper;

    @Value("${customer_count}")
    private int customerCount = 0;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private volatile boolean running = true;

    private List<Thread> threadList;

    @Override
    public void afterPropertiesSet() {
        threadList = new ArrayList<>();
        for (int i = 0; i < customerCount; i++) {
            Thread thread = new Thread(this, "worker" + i);
            thread.start();
            threadList.add(thread);
        }
    }

    @Override
    public void destroy() throws Exception {
        running = false;
        for (Thread aThreadList : threadList) {
            aThreadList.join();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                boolean res = worker();
                if (!res) {
                    Thread.sleep(3000);
                }
            }catch (Exception e) {
                logger.error("worker error: ", e);
            }
        }
        logger.debug("进程停止" + Thread.currentThread().getName());
    }

    /**
     * 执行消息，如果能够获取到消息，则返回true，否在返回false
     * @return bool
     */
    private boolean worker() {
        // 1. 推出消息
        Message message = pop();
        if (message == null) {
            return false;
        }

        // 2. 执行消息
        long start = System.currentTimeMillis();
        try {
            String response = runMessage(message);
            // 3. 保存执行结果
            messageSuccess(message, response, (int)(System.currentTimeMillis() - start));
        } catch (Exception e) {
            // 3. 保存执行结果
            messageFiled(message, e.getMessage(), (int)(System.currentTimeMillis() - start));
        }
        return true;
    }

    private String runMessage(Message message) throws Exception {

        Customer customer = customerMapper.getById(message.getCustomerId());

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(customer.getTimeout(), TimeUnit.SECONDS)
                .build();

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), message.getMessage());
        Request request = new Request.Builder()
                .url(customer.getApi())
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    @Transactional
    private Message pop() {
        Message message = messageMapper.popMessage();
        if (message == null) {
            return null;
        }
        Customer customer = customerMapper.getById(message.getCustomerId());
        messageMapper.start(message.getId(), Utils.getCurrentTimestamp() + customer.getTimeout());
        return message;
    }

    @Transactional
    private void messageSuccess(Message message, String response, Integer time) {
        // 生成执行日志
        MessageLog log = new MessageLog();
        log.setCreateDate((int)(System.currentTimeMillis() / 1000));
        log.setResponse(response);
        log.setCustomerId(message.getCustomerId());
        log.setMessageId(message.getId());
        log.setTopicId(message.getTopicId());
        log.setTime(time);
        log.setError("");
        messageLogMapper.create(log);

        // 修改消息状态
        messageMapper.success(message.getId());
    }

    @Transactional
    private void messageFiled(Message message, String error, Integer time) {
        // 生成执行日志
        MessageLog log = new MessageLog();
        log.setCreateDate((int)(System.currentTimeMillis() / 1000));
        log.setError(error);
        log.setCustomerId(message.getCustomerId());
        log.setMessageId(message.getId());
        log.setTopicId(message.getTopicId());
        log.setTime(time);
        log.setResponse("");
        messageLogMapper.create(log);

        Customer customer = customerMapper.getById(message.getCustomerId());
        if (message.getAttempts() >= customer.getAttempts()) {
            // 如果
            messageMapper.failed(message.getId());
        } else {
            messageMapper.retry(message.getId());
        }
    }

}
