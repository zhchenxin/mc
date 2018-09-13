package top.chenxin.mc.web.response.customer;

import top.chenxin.mc.dao.po.Customer;
import top.chenxin.mc.dao.po.Message;
import top.chenxin.mc.dao.po.Topic;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.service.dto.CustomerSearchList;
import top.chenxin.mc.web.response.PaginationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListResponse extends PaginationResponse {

    private CustomerSearchList customerSearchList;

    public ListResponse(CustomerSearchList customerSearchList) {
        this.customerSearchList = customerSearchList;
        this.setPage(customerSearchList.getCustomerList());
    }

    @Override
    protected List getList() {
        List<Map<String, Object>> list = new ArrayList<>();

        for (Customer item :customerSearchList.getCustomerList()) {
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
        return this.customerSearchList.getTopicMap().get(customer.getTopicId());
    }

}


