package top.zhchenxin.mc.console;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.zhchenxin.mc.service.MessageService;

@Component
public class RetryTimeoutJob implements DisposableBean, Runnable {
    private Thread thread;
    private volatile boolean someCondition = true;

    private MessageService messageService;

    @Autowired
    public RetryTimeoutJob(MessageService messageService) {
        this.messageService = messageService;

        this.thread = new Thread(this);
        this.thread.setName("retry-timeout-job");
        this.thread.start();
    }

    @Override
    public void run() {
        while (someCondition) {
            this.messageService.retryTimeoutMessage();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        this.someCondition = false;
    }
}
