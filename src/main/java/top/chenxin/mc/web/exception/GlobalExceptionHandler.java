package top.chenxin.mc.web.exception;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.chenxin.mc.common.constant.ErrorCode;
import top.chenxin.mc.web.response.ErrorResponse;
import top.chenxin.mc.service.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public Map validationHandle(HttpServletRequest req, BindException e) {
        e.printStackTrace();
        return new ErrorResponse(ErrorCode.VALIDAT_ERROR, e.getAllErrors().get(0).getDefaultMessage()).toMap();
    }

    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    public Map ServiceExceptionHandler(HttpServletRequest req, ServiceException e) {
        e.printStackTrace();
        return new ErrorResponse(e.getCode(), e.getMessage()).toMap();
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Map exceptionHandler(HttpServletRequest req, Exception e) {
        e.printStackTrace();
        return new ErrorResponse(1, e.getMessage()).toMap();
    }
}
