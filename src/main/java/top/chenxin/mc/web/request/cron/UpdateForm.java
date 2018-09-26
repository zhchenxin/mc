package top.chenxin.mc.web.request.cron;

import javax.validation.constraints.NotNull;

public class UpdateForm {
    @NotNull
    private String name;
    @NotNull
    private String description;

    @NotNull
    private String spec;

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
}
