package top.chenxin.mc.response;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractResponse implements Response {

    protected abstract Map getData();

    @Override
    public Map toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "操作成功");
        map.put("data", getData());
        return map;
    }
}
