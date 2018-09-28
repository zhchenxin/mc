package top.chenxin.mc.web.response.topic;

import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.web.response.AbstractResponse;

import java.util.HashMap;
import java.util.Map;

public class UpdateResponse extends AbstractResponse {

    private Topic topic;

    public UpdateResponse(Topic topic) {
        this.topic = topic;
    }

    @Override
    protected Map getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("topic", getTopic());
        return map;
    }

    private Map getTopic() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", topic.getId());
        map.put("name", topic.getName());
        map.put("description", topic.getDescription());
        return map;
    }
}
