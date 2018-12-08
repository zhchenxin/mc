package top.chenxin.mc.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import top.chenxin.mc.service.model.PageList;

public class APIResponse {

    /**
     * 请求操作成功的时候返回
     */
    public static ResponseEntity<JSONObject> success() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("message", "ok");
        return new ResponseEntity<>(jsonObject, HttpStatus.OK);
    }

    /**
     * 返回列表数据
     * @param pageList 列表数据
     */
    public static ResponseEntity<JSONObject> success(PageList pageList) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("message", "ok");

        JSONObject data = new JSONObject();

        // mate 数据
        JSONObject mate = new JSONObject();
        mate.put("total", pageList.getTotal());
        mate.put("totalPage", Math.ceil((float)pageList.getTotal() / (float)pageList.getLimit()));
        mate.put("currentPage", pageList.getPage());
        mate.put("limit", pageList.getLimit());
        data.put("mate", mate);

        // list 数据
        data.put("list", pageList);

        jsonObject.put("data", data);

        return new ResponseEntity<>(jsonObject, HttpStatus.OK);
    }

    /**
     * 返回单个实体
     * @param data 实体
     */
    public static ResponseEntity<JSONObject> success(Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("message", "ok");
        jsonObject.put("data", data);
        return new ResponseEntity<>(jsonObject, HttpStatus.OK);
    }

    /**
     * 异常返回
     * @param code    错误码
     * @param message 错误描述
     */
    public static ResponseEntity<JSONObject> error(int code, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("message", message);
        return new ResponseEntity<>(jsonObject, HttpStatus.OK);
    }

}
