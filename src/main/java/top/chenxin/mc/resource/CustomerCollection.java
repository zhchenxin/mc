package top.chenxin.mc.resource;

import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.lib.PaginationCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerCollection extends PaginationCollection<Customer> {

    private Map<Long, Topic> topicMap;

    @Override
    protected List formatList() {
        List<Map<String, Object>> list = new ArrayList<>();

        for (int i = 0 ; i < getList().size(); i++) {
            Customer item = getList().get(i);

            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getId());
            map.put("name", item.getName());
            map.put("api", item.getApi());
            map.put("timeout", item.getTimeout());
            map.put("attempts", item.getAttempts());
            map.put("createDate", item.getCreateDate());
            map.put("topicId", item.getTopicId());
            map.put("topicName", topicMap.get(item.getTopicId()).getName());

            list.add(map);
        }

        return list;
    }

    public void setTopicList(List<Topic> topicList) {
        topicMap = new HashMap<>();

        for (Topic item : topicList) {
            topicMap.put(item.getId(), item);
        }
    }
}


