package top.chenxin.mc.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
     * 如果没有, 则返回 null
     */
    public static List<Long> getIds(String id) {
        if (id == null) {
            return null;
        }
        String [] stringArr= id.split(",");
        if (stringArr.length == 0) {
            return null;
        }
        List<Long> ids = new ArrayList<>();
        for (String aStringArr : stringArr) {
            ids.add(Long.parseLong(aStringArr));
        }
        return ids;
    }

    /**
     * 获取当前时间戳
     * @return 当前时间戳
     */
    public static int getCurrentTimestamp() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * 将时间戳转换成时间
     * @return 时间字符串
     */
    public static String simpleDate(int timestamp) {
        Date date = new Date((long)timestamp * 1000);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
}
