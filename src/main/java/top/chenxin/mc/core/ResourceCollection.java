package top.chenxin.mc.core;

import com.github.pagehelper.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceCollection<T extends Resource> {

    private List<T> resources;
    private Long page;
    private Long limit;
    private Long total;

    public ResourceCollection(List<T> resources, Page page) {
        this(resources, (long)page.getPageNum(), (long)page.getPageSize(), page.getTotal());
    }

    public ResourceCollection(List<T> resources, Long page, Long limit, Long total) {
        this.resources = resources;
        this.page = page;
        this.limit = limit;
        this.total = total;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "操作成功");

        Map<String, Object> data = new HashMap<>();

        // list
        ArrayList list = new ArrayList();
        for (T resource :resources) {
            list.add(resource.getData());
        }
        data.put("list", list);

        // mate
        Map<String, Object> mate = new HashMap<>();
        mate.put("total", total);
        mate.put("totalPage", Math.ceil((float)total / (float)limit));
        mate.put("currentPage", page);
        mate.put("limit", limit);
        data.put("mate", mate);

        map.put("data", data);
        return map;
    }
}
