package top.zhchenxin.mc.resource;

import top.zhchenxin.mc.entity.Customer;
import top.zhchenxin.mc.entity.Message;
import top.zhchenxin.mc.entity.Topic;
import top.zhchenxin.mc.lib.PaginationCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerCollection extends PaginationCollection<Customer> {

    private Map<Long, Topic> topicMap;

    @Override
    protected List formatList() {
        List<Map<String, Object>> list = new ArrayList<>();

        for (int i = 0 ; i < this.getList().size(); i++) {
            Customer item = this.getList().get(i);

            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getId());
            map.put("name", item.getName());
            map.put("api", item.getApi());
            map.put("timeout", item.getTimeout());
            map.put("attempts", item.getAttempts());
            map.put("createDate", item.getCreateDate());
            map.put("topicId", item.getTopicId());
            map.put("topicName", this.topicMap.get(item.getTopicId()).getName());

            list.add(map);
        }

        return list;
    }

    public void setTopicList(List<Topic> topicList) {
        this.topicMap = new HashMap<>();

        for (Topic item : topicList) {
            topicMap.put(item.getId(), item);
        }
    }
}


