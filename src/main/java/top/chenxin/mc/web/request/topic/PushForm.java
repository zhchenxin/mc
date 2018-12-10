package top.chenxin.mc.web.request.topic;

public class PushForm {
    private String message = "";
    private Integer delay = 0;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }
}
