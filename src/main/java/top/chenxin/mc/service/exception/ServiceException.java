package top.chenxin.mc.service.exception;

public class ServiceException extends RuntimeException {

    private Integer code = 1;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
