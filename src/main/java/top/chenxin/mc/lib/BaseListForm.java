package top.chenxin.mc.lib;

public class BaseListForm {

    private Integer page = 1;
    private Integer limit = 10;
    private Integer offset = 0;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
        this.offset = (this.page - 1) * this.limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
        this.offset = (this.page - 1) * this.limit;
    }

    public Integer getOffset() {
        return offset;
    }
}
