package top.chenxin.mc.response;

import com.github.pagehelper.Page;

import java.util.List;

public class EmptyPaginationResponse extends PaginationResponse {

    public EmptyPaginationResponse(Page page) {
        this.setPage(page);
    }

    protected List formatList() {
        return null;
    }
}
