package top.zhchenxin.mc.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.zhchenxin.mc.service.MessageService;

@Component
public class RetryTimeoutJob implements Runnable, InitializingBean, DisposableBean {

    @Autowired
    private MessageService messageService;

    private Thread thread;

    private volatile boolean running = true;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void afterPropertiesSet() throws Exception {
        thread = new Thread(this);
        thread.setName("retry-timeout-job");
        thread.start();
    }

    @Override
    public void destroy() throws Exception {
        this.running = false;
        thread.join();
    }

    @Override
    public void run() {
        while (running) {
            this.messageService.retryTimeoutMessage();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.debug("进程停止" + Thread.currentThread().getName());
    }
}
