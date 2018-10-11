package top.chenxin.mc.controller;


import java.util.HashMap;
import java.util.Map;

abstract class BaseController {
    protected Map success() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "处理成功");
        return map;
    }
}
