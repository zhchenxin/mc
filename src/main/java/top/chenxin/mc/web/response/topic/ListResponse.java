package top.chenxin.mc.web.response.topic;

import com.github.pagehelper.Page;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.web.response.PaginationResponse;
import top.chenxin.mc.entity.Topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListResponse extends PaginationResponse {

    private Page<Topic> topicList;

    public ListResponse(Page<Topic> topicList) {
        this.topicList = topicList;
        this.setPage(topicList);
    }

    protected List getList() {
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
