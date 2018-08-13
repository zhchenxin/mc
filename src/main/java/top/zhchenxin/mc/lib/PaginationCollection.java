package top.zhchenxin.mc.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaginationCollection<T extends BaseEntity> {

    private List<T> list;
    private Long count;
    private Long page;
    private Long limit;

    protected List formatList() {
        return this.list;
    }

    public Map<String, Object> output() {
        Map<String, Object> map = new HashMap<>();
        map.put("mate", this.getMate());
        map.put("list", this.formatList());
        return map;
    }

    private Map<String, Long> getMate() {
        Map<String, Long> mate = new HashMap<>();
        mate.put("total", this.count);
        mate.put("totalPage", this.getTotalPage());
        mate.put("currentPage", this.page);
        mate.put("limit", this.limit);
        return mate;
    }

    private Long getTotalPage() {
        return (long) Math.ceil((double)this.count / (double)this.limit);
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public List<Long> getIds() {
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < this.list.size(); i++) {
            ids.add(this.list.get(i).getId());
        }
        return ids;
    }
}
