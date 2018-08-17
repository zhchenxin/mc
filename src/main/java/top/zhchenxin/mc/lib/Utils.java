package top.zhchenxin.mc.lib;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Utils {

    /**
     * 移除列表中重复元素
     */
    public static <T> void removeDuplicate(List<T> list) {
        Set<T> set = new LinkedHashSet<>(list.size());
        set.addAll(list);
        list.clear();
        list.addAll(set);
    }

    /**
     * 获取当前时间戳
     * @return
     */
    public static int getCurrentTimestamp() {
        return (int) (System.currentTimeMillis() / 1000);
    }
}
