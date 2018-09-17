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
        logger.info("start");
        thread = new Thread(this);
        thread.setName("retry_timeout_job");
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
            try {
                messageService.retryTimeoutMessage();
                Thread.sleep(5 * 60 * 1000);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        logger.debug("进程停止" + Thread.currentThread().getName());
    }
}
