package top.zhchenxin.mc.lib;

import java.util.HashMap;
import java.util.Map;

public class BaseController {
    protected Map<String, Object> successJson(Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "操作成功");
        map.put("data", data);
        return map;
    }
}
