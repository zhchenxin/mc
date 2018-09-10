package top.chenxin.mc.lib;

import java.util.Map;

public interface Resource {

    /**
     * 将模型数据输出成为 json 数据
     */
    Map<String, Object> toMap();
}
