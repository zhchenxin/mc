package top.chenxin.mc.core;

import java.util.HashMap;
import java.util.Map;

public abstract class Resource {

    protected abstract Map<String, Object> getData();

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "操作成功");
        map.put("data", getData());
        return map;
    }
}
