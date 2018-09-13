package top.chenxin.mc.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MessageDaoTest {

    @Autowired
    private MessageDao messageDao;

    @Test
    public void popMessage() {
        messageDao.popMessage();
    }

    @Test
    public void retryTimeoutMessage() {
        messageDao.retryTimeoutMessage();
    }

    @Test
    public void start() {
        messageDao.start(1L, 1536571633);
    }

    @Test
    public void failed() {
        messageDao.failed(1L);
    }

    @Test
    public void retry() {
        messageDao.failed(1L);
    }

    @Test
    public void success() {
        messageDao.failed(1L);
    }

}
