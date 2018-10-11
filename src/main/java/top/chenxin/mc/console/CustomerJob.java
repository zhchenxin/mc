package top.chenxin.mc.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import top.chenxin.mc.service.MessageService;

import java.util.ArrayList;
import java.util.List;


@Component
@Configuration
public class CustomerJob implements Runnable, InitializingBean, DisposableBean {

    @Autowired
    private MessageService messageService;

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
                boolean res = messageService.popMessage();
                if (!res) {
                    Thread.sleep(3000);
                }
            }catch (Exception e) {
                logger.error("worker error: ", e);
            }
        }
        logger.debug("进程停止" + Thread.currentThread().getName());
    }
}
