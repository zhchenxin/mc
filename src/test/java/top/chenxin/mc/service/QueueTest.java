package top.chenxin.mc.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.chenxin.mc.service.model.MessageModel;
import top.chenxin.mc.service.queue.Queue;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class QueueTest {

    @Autowired
    private Queue queue;

    @Test
    public void push() {
        MessageModel model = new MessageModel();
        model.setId(1L);
        model.setTopicId(1L);
        model.setCustomerId(2L);
        model.setAttempts(1);
        model.setMaxAttempts(3);
        model.setTimeout(60);
        model.setDelay(0);
        model.setMessage("");
        model.setApi("api");

        queue.push(model);
    }

    @Test
    public void pop() {
        queue.pop();
    }

}