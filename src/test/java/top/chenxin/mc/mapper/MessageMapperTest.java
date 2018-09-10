package top.chenxin.mc.mapper;

import org.apache.ibatis.annotations.Param;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MessageMapperTest {

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void popMessage() {
        messageMapper.popMessage();
    }

    @Test
    public void retryTimeoutMessage() {
        messageMapper.retryTimeoutMessage();
    }

    @Test
    public void start() {
        messageMapper.start(1L, 1536571633);
    }

    @Test
    public void failed() {
        messageMapper.failed(1L);
    }

    @Test
    public void retry() {
        messageMapper.failed(1L);
    }

    @Test
    public void success() {
        messageMapper.failed(1L);
    }

}
