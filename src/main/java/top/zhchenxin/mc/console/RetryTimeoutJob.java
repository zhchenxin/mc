package top.zhchenxin.mc.console;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.zhchenxin.mc.service.MessageService;

@Component
public class RetryTimeoutJob implements Runnable, InitializingBean {

    @Autowired
    private MessageService messageService;

    @Override
    public void afterPropertiesSet() throws Exception {
        Thread thread = new Thread(this);
        thread.setName("retry-timeout-job");
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            this.messageService.retryTimeoutMessage();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
