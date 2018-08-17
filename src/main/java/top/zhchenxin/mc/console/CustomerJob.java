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
import top.zhchenxin.mc.resource.MessageDetail;
import top.zhchenxin.mc.service.CustomerService;
import top.zhchenxin.mc.service.MessageService;

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
        MessageDetail message = messageService.pop();
        if (message == null) {
            return false;
        }

        // 2. 执行消息
        long start = System.currentTimeMillis();
        try {
            String response = runMessage(message);
            // 3. 保存执行结果
            messageService.messageSuccess(message.getEntity().getId(), response, (int)(System.currentTimeMillis() - start));
        } catch (Exception e) {
            // 3. 保存执行结果
            messageService.messageFiled(message.getEntity().getId(), e.getMessage(), (int)(System.currentTimeMillis() - start));        }
        return true;
    }

    private String runMessage(MessageDetail message) throws Exception {
        OkHttpClient client = new OkHttpClient();
        client.setReadTimeout(message.getCustomer().getTimeout(), TimeUnit.SECONDS);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, message.getEntity().getMessage());
        Request request = new Request.Builder()
                .url(message.getCustomer().getApi())
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
