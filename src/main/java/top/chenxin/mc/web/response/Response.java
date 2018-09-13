package top.chenxin.mc.web.response;

import java.util.Map;

public interface Response {

    /**
     * 将模型数据输出成为 json 数据
     */
    Map toMap();
}
