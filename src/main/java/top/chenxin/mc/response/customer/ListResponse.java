package top.chenxin.mc.response.customer;

import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.lib.Utils;
import top.chenxin.mc.response.PaginationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListResponse extends PaginationResponse {

    private Map<Long, Topic> topicMap;
    private List<Customer> customerList;

    public ListResponse(List<Customer> customerList, List<Topic> topicList) {
        this.customerList = customerList;
        this.topicMap = new HashMap<>();
        for (Topic item : topicList) {
            this.topicMap.put(item.getId(), item);
        }
    }

    @Override
    protected List getList() {
        List<Map<String, Object>> list = new ArrayList<>();

        for (Customer item : customerList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getId());
            map.put("name", item.getName());
            map.put("api", item.getApi());
            map.put("timeout", item.getTimeout());
            map.put("attempts", item.getAttempts());
            map.put("createDate", Utils.simpleDate(item.getCreateDate()));
            map.put("topicName", topicMap.get(item.getTopicId()).getName());

            list.add(map);
        }

        return list;
    }
}


