package top.chenxin.mc.lib;

public class BaseListForm {

    private Long page = 1L;
    private Long limit = 10L;
    private Long offset = 0L;

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
        this.offset = (this.page - 1) * this.limit;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
        this.offset = (this.page - 1) * this.limit;
    }

    public Long getOffset() {
        return offset;
    }
}
