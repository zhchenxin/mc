package top.chenxin.mc.exception;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.chenxin.mc.common.constant.ErrorCode;
import top.chenxin.mc.service.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public Map validationHandle(HttpServletRequest req, BindException e) {
        e.printStackTrace();
        return returnError(ErrorCode.VALIDAT_ERROR, e.getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    public Map ServiceExceptionHandler(HttpServletRequest req, ServiceException e) {
        e.printStackTrace();
        return returnError(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Map exceptionHandler(HttpServletRequest req, Exception e) {
        e.printStackTrace();
        return returnError(1, e.getMessage());
    }

    private Map returnError(int code, String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }
}
