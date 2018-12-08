package top.chenxin.mc.service.model;

import com.github.pagehelper.Page;

import java.util.ArrayList;
import java.util.List;

public class PageList<T> extends ArrayList<T> {

    // 总页数
    private Long page;

    // 每页大小
    private Long limit;

    // 列表总数量
    private Long total;

    public PageList(Page<T> page) {
        this(page, (long)page.getPageNum(), (long)page.getPageSize(), page.getTotal());
    }

    public PageList(List<T> list, Long page, Long limit, Long total) {
        this.page = page;
        this.limit = limit;
        this.total = total;

        this.addAll(list);
    }

    public Long getPage() {
        return page;
    }

    public Long getLimit() {
        return limit;
    }

    public Long getTotal() {
        return total;
    }

    //    public Map<String, Object> toMap() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("code", 0);
//        map.put("msg", "操作成功");
//
//        Map<String, Object> data = new HashMap<>();
//
//        // list
//        List<Map> list = new ArrayList<>();
//        for (T resource :resources) {
//            list.add(resource.getData());
//        }
//        data.put("list", list);
//
//        // mate
//        Map<String, Object> mate = new HashMap<>();
//        mate.put("total", total);
//        mate.put("totalPage", Math.ceil((float)total / (float)limit));
//        mate.put("currentPage", page);
//        mate.put("limit", limit);
//        data.put("mate", mate);
//
//        map.put("data", data);
//        return map;
//    }
}
