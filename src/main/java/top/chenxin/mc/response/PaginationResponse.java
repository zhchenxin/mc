package top.chenxin.mc.response;

import com.github.pagehelper.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页输出
 */
public abstract class PaginationResponse implements Response {

    private Page page;

    protected abstract List formatList();

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("mate", getMate());
        map.put("list", formatList());
        return map;
    }

    private Map<String, Object> getMate() {
        Map<String, Object> mate = new HashMap<>();
        // 总个数
        mate.put("total", page.getTotal());
        mate.put("totalPage", page.getPages());
        mate.put("currentPage", page.getPageNum());
        mate.put("limit", page.getPageSize());
        return mate;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}