package top.chenxin.mc.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Map<String, Object> exceptionHandler(HttpServletRequest req, Exception e) {
        e.printStackTrace();
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("code", 1);
        modelMap.put("msg", e.getMessage());
        return modelMap;
    }
}
