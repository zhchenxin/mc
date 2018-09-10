package top.chenxin.mc.response.topic;

import top.chenxin.mc.lib.Utils;
import top.chenxin.mc.response.PaginationResponse;
import top.chenxin.mc.entity.Topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListResponse extends PaginationResponse {

    private List<Topic> topicList;

    public ListResponse(List<Topic> topicList) {
        this.topicList = topicList;
    }

    protected List formatList() {
        List<Map<String, Object>> list = new ArrayList<>();

        for (Topic topic : topicList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", topic.getId());
            map.put("name", topic.getName());
            map.put("description", topic.getDescription());
            map.put("createDate", Utils.simpleDate(topic.getCreateDate()));
            list.add(map);
        }

        return list;
    }
}
