package top.chenxin.mc.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import top.chenxin.mc.service.model.MessageModel;
import top.chenxin.mc.service.CustomerService;
import top.chenxin.mc.service.MessageService;

import java.util.ArrayList;
import java.util.List;


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
                    Thread.sleep(300);
                }
            }catch (Exception e) {
                logger.error("worker error: ", e);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        logger.debug("进程停止" + Thread.currentThread().getName());
    }

    private boolean worker() {
        // 1. 推出消息
        MessageModel message = pop();
        if (message == null) {
            return false;
        }

        // 2. 执行消息
        long start = System.currentTimeMillis();
        try {
            String response = runMessage(message);
            // 3. 保存执行结果
            messageService.messageSuccess(message, response, (int)(System.currentTimeMillis() - start));
        } catch (Exception e) {
            // 3. 保存执行结果
            messageService.messageFiled(message, e.getMessage(), (int)(System.currentTimeMillis() - start));
        }
        return true;
    }

    private synchronized MessageModel pop() {
        return messageService.pop();
    }

    private String runMessage(MessageModel message) {
        return sendPost(message.getApi(), message.getMessage(), message.getTimeout());
    }

    private String sendPost(String url, String message, int timeout) {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(timeout * 1000);
        requestFactory.setReadTimeout(timeout * 1000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate.getForObject(url + "?msg=" + message, String.class);
    }
}
