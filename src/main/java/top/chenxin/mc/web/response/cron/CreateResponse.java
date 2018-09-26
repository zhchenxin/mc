package top.chenxin.mc.web.response.cron;

import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.web.response.AbstractResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateResponse extends AbstractResponse {

    private List<Topic> topicList;

    public CreateResponse(List<Topic> topicList) {
        this.topicList = topicList;
    }

    @Override
    protected Map getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("topicList", getTopic());
        return map;
    }

    private List getTopic() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Topic topic : this.topicList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", topic.getId());
            map.put("name", topic.getName());
            list.add(map);
        }
        return list;
    }
}
