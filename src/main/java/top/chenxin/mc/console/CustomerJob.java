package top.chenxin.mc.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import top.chenxin.mc.entity.Message;
import top.chenxin.mc.resource.CustomerResource;
import top.chenxin.mc.service.CustomerService;
import top.chenxin.mc.service.MessageService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
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
                    Thread.sleep(3000);
                }
            }catch (Exception e) {
                logger.error("worker error: ", e);
            }
        }
        logger.debug("进程停止" + Thread.currentThread().getName());
    }

    private boolean worker() {
        // 1. 推出消息
        Message message = pop();
        if (message == null) {
            return false;
        }

        // 2. 执行消息
        long start = System.currentTimeMillis();
        try {
            String response = runMessage(message);
            // 3. 保存执行结果
            messageService.messageSuccess(message.getId(), response, (int)(System.currentTimeMillis() - start));
        } catch (Exception e) {
            // 3. 保存执行结果
            messageService.messageFiled(message.getId(), e.getMessage(), (int)(System.currentTimeMillis() - start));
        }
        return true;
    }

    private synchronized Message pop() {
        return messageService.pop();
    }

    private String runMessage(Message message) throws Exception {
        CustomerResource customer = customerService.getById(message.getCustomerId());
        return sendPost(customer.getApi(), message.getMessage());
    }

    private String sendPost(String url, String param) throws IOException {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader( new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            throw e;
        } finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            } catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
}
