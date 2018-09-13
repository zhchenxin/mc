package top.chenxin.mc.web.response;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponse implements Response {

    private Integer code;
    private String msg;

    public ErrorResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Map toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }
}
