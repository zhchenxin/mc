package top.chenxin.mc.web.response.customer;

import top.chenxin.mc.entity.Customer;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.web.response.AbstractResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateResponse extends AbstractResponse {

    private Customer customer;

    public UpdateResponse(Customer customer) {
        this.customer = customer;
    }

    @Override
    protected Map getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("customer", getCustomer());
        return map;
    }

    private Map getCustomer() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", customer.getName());
        map.put("api", customer.getApi());
        map.put("attempts", customer.getAttempts());
        map.put("timeout", customer.getTimeout());
        return map;
    }
}
