package top.chenxin.mc.request.topic;

import top.chenxin.mc.request.BaseListForm;

public class ListForm extends BaseListForm {
    private String name;

    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
