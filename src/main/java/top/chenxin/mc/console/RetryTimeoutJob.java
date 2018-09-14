package top.chenxin.mc.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.chenxin.mc.service.MessageService;

/**
 * 将超时的消息设置为等待中状态
 */
@Component
public class RetryTimeoutJob implements Runnable, InitializingBean, DisposableBean {

    @Autowired
    private MessageService messageService;

    private Thread thread;

    private volatile boolean running = true;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void afterPropertiesSet() {
        thread = new Thread(this);
        thread.setName("retry-timeout-job");
        thread.start();
    }

    @Override
    public void destroy() throws Exception {
        running = false;
        thread.join();
    }

    @Override
    public void run() {
        while (running) {
            messageService.retryTimeoutMessage();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.debug("进程停止" + Thread.currentThread().getName());
    }
}
