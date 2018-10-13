package top.chenxin.mc.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(getClass());


    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public Map validationHandle(HttpServletRequest req, BindException e) {
        return error(ErrorCode.VALIDAT_ERROR, e.getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    public Map ServiceExceptionHandler(HttpServletRequest req, ServiceException e) {
        logger.error("服务异常: 原因", e);
        return error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Map exceptionHandler(HttpServletRequest req, Exception e) {
        logger.error("未知异常: 原因", e);
        return error(1, e.getMessage());
    }

    private Map error(int code, String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }
}
