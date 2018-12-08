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
}
