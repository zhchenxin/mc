package top.chenxin.mc.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页输出
 */
public abstract class PaginationResponse implements Response {

    private Long count;
    private Long page;
    private Long limit;

    protected abstract List formatList();

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("mate", getMate());
        map.put("list", formatList());
        return map;
    }

    private Map<String, Long> getMate() {
        Map<String, Long> mate = new HashMap<>();
        mate.put("total", count);
        mate.put("totalPage", getTotalPage());
        mate.put("currentPage", page);
        mate.put("limit", limit);
        return mate;
    }

    private Long getTotalPage() {
        return (long) Math.ceil((double)count / (double)limit);
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }
}
