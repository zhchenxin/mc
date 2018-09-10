package top.chenxin.mc.response;

import java.util.List;

public class EmptyPaginationResponse extends PaginationResponse {
    protected List formatList() {
        return null;
    }
}
