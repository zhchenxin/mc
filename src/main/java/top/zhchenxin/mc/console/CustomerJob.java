package top.zhchenxin.mc.console;

import com.squareup.okhttp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.zhchenxin.mc.entity.Customer;
import top.zhchenxin.mc.entity.Message;
import top.zhchenxin.mc.service.CustomerService;
import top.zhchenxin.mc.service.MessageService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


@Component
public class CustomerJob implements DisposableBean, Runnable {
    private volatile boolean someCondition;

    private MessageService messageService;
    private CustomerService customerService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public CustomerJob(CustomerService customerService, MessageService messageService) {

        this.customerService = customerService;
        this.messageService = messageService;

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(this);
            thread.setName("worker" + i);
            thread.start();
        }
        this.someCondition = true;
    }

    @Override
    public void run() {
        try {
            while (someCondition) {
                boolean res = worker();
                if (!res) {
                    Thread.sleep(3000);
                }
            }
        }catch (Exception e) {
            logger.error("worker error: ", e);
        }
    }

    @Override
    public void destroy() throws Exception {
        someCondition = false;
    }

    /**
     * 执行消息，如果能够获取到消息，则返回true，否在返回false
     * @return bool
     */
    private boolean worker() {
        // 1. 推出消息
        Message message = this.messageService.pop();
        if (message == null) {
            return false;
        }
        Customer customer = this.customerService.getById(message.getCustomerId());

        // 2. 执行消息
        long start = System.currentTimeMillis();
        try {
            String response = this.runMessage(message, customer);
            // 3. 保存执行结果
            this.messageService.messageSuccess(message.getId(), response, (int)(System.currentTimeMillis() - start));
        } catch (IOException e) {
            // 3. 保存执行结果
            this.messageService.messageFiled(message.getId(), e.getMessage(), (int)(System.currentTimeMillis() - start));
        }
        return true;
    }

    private String runMessage(Message message, Customer customer) throws IOException {
        OkHttpClient client = new OkHttpClient();
        client.setReadTimeout(customer.getTimeout(), TimeUnit.SECONDS);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, message.getMessage());
        Request request = new Request.Builder()
                .url(customer.getApi())
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
