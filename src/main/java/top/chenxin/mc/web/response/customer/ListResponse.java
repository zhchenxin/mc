package top.chenxin.mc.web.response.customer;

import com.github.pagehelper.Page;
import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.web.response.PaginationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListResponse extends PaginationResponse {

    private Map<Long, Topic> topicMap;
    private Page<Customer> customerList;

    public ListResponse(Page<Customer> customerList, List<Topic> topicList) {
        this.customerList = customerList;
        this.setPage(customerList);

        this.topicMap = new HashMap<>();
        if (topicList != null ) {
            for (Topic item : topicList) {
                this.topicMap.put(item.getId(), item);
            }
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
            map.put("topicName", getTopic(item).getName());

            list.add(map);
        }

        return list;
    }

    private Topic getTopic(Customer customer) {
        return topicMap.get(customer.getTopicId());
    }

}


