package top.zhchenxin.mc.form.topic;

import top.zhchenxin.mc.entity.Topic;

import javax.validation.constraints.NotNull;

public class CreateForm {
    @NotNull
    private String name;
    @NotNull
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Topic toTopic() {
        Topic topic = new Topic();
        topic.setName(this.name);
        topic.setDescription(this.description);
        topic.setCreateDate((int) (System.currentTimeMillis() / 1000));
        return topic;
    }
}
