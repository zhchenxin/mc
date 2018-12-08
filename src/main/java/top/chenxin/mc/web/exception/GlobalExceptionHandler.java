package top.chenxin.mc.web.exception;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.chenxin.mc.common.constant.ErrorCode;
import top.chenxin.mc.service.exception.ServiceException;
import top.chenxin.mc.web.APIResponse;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ResponseEntity<JSONObject> validationHandle(HttpServletRequest req, BindException e) {
        return APIResponse.error(ErrorCode.VALIDAT_ERROR, e.getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    public ResponseEntity<JSONObject> ServiceExceptionHandler(HttpServletRequest req, ServiceException e) {
        logger.error("服务异常: 原因", e);
        return APIResponse.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<JSONObject> exceptionHandler(HttpServletRequest req, Exception e) {
        logger.error("未知异常: 原因", e);
        return APIResponse.error(1, e.getMessage());
    }

}
