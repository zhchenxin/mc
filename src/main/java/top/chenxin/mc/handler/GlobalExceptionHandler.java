package top.chenxin.mc.handler;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.chenxin.mc.response.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public Map validationHandle(HttpServletRequest req, BindException e) {
        e.printStackTrace();
        return new ErrorResponse(1, e.getAllErrors().get(0).getDefaultMessage()).toMap();
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Map exceptionHandler(HttpServletRequest req, Exception e) {
        e.printStackTrace();
        return new ErrorResponse(1, e.getMessage()).toMap();
    }
}
