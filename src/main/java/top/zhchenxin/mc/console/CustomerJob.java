package top.zhchenxin.mc.console;

import com.squareup.okhttp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import top.zhchenxin.mc.entity.Customer;
import top.zhchenxin.mc.entity.Message;
import top.zhchenxin.mc.service.CustomerService;
import top.zhchenxin.mc.service.MessageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Component
@Configuration
public class CustomerJob implements Runnable, InitializingBean, DisposableBean {

    @Autowired
    private MessageService messageService;

    @Autowired
    private CustomerService customerService;

    @Value("${customer_count}")
    private int customerCount = 0;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private volatile boolean running = true;

    private List<Thread> threadList;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.threadList = new ArrayList<>();
        for (int i = 0; i < this.customerCount; i++) {
            Thread thread = new Thread(this, "worker" + i);
            thread.start();
            this.threadList.add(thread);
        }
    }

    @Override
    public void destroy() throws Exception {
        this.running = false;
        while (true) {
            Thread.sleep(100);
            boolean isStop = true;

            for (int i = 0; i < this.threadList.size(); i++) {
                if (this.threadList.get(i).getState() != Thread.State.TERMINATED) {
                    isStop = false;
                    break;
                }
            }

            if (isStop) {
                break;
            }
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
