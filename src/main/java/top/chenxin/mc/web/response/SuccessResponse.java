package top.chenxin.mc.web.response;

import java.util.Map;

/**
 * 操作成功的返回值, 用于没有返回数据的返回值
 */
public class SuccessResponse extends AbstractResponse {
    @Override
    protected Map getData() {
        return null;
    }
}
