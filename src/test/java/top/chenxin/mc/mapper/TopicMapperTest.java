package top.chenxin.mc.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.chenxin.mc.entity.Topic;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TopicMapperTest {

    @Autowired
    private TopicMapper topicMapper;

    @Test
    public void create() {
        Topic topic = new Topic();
        topic.setName("test");
        topic.setDescription("test");
        topicMapper.create(topic);
    }

    @Test
    public void getByName() {
        topicMapper.getByName("test");
    }

    @Test
    public void getById() {
        topicMapper.getById(1L);
    }

    @Test
    public void getByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        topicMapper.getByIds(ids);
    }

}
