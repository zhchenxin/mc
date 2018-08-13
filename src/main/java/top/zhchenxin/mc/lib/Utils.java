package top.zhchenxin.mc.lib;

import java.util.LinkedHashSet;
import java.util.List;

public class Utils {
    public static <T> void removeDuplicate(List<T> list) {
        LinkedHashSet<T> set = new LinkedHashSet<>(list.size());
        set.addAll(list);
        list.clear();
        list.addAll(set);
    }
}
