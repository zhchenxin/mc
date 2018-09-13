package top.chenxin.mc.dao;

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
public class TopicDaoTest {

    @Autowired
    private TopicDao topicDao;

    @Test
    public void create() {
        Topic topic = new Topic();
        topic.setName("test");
        topic.setDescription("test");
        topicDao.insert(topic);
    }

    @Test
    public void getByName() {
        topicDao.getByName("test");
    }

    @Test
    public void getById() {
        topicDao.getById(1L);
    }

    @Test
    public void getByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        topicDao.getByIds(ids);
    }

}
