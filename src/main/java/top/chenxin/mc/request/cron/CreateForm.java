package top.chenxin.mc.request.cron;

import javax.validation.constraints.NotNull;

public class CreateForm {
    @NotNull(message = "name 不能为空")
    private String name;
    @NotNull(message = "description 不能为空")
    private String description;
    @NotNull(message = "spec 不能为空")
    private String spec;
    @NotNull(message = "topic id 不能为空")
    private Long topicId;

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

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }
}
