package top.chenxin.mc.resource;

import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.core.Resource;
import top.chenxin.mc.entity.Topic;

import java.util.HashMap;
import java.util.Map;

public class TopicResource extends Resource {

    private Long id;
    private String name;
    private String description;
    private Integer createDate;
    private Integer updateDate;

    public TopicResource(Topic topic) {
        this.id = topic.getId();
        this.name = topic.getName();
        this.description = topic.getDescription();
        this.createDate = topic.getCreateDate();
        this.updateDate = topic.getUpdateDate();
    }

    @Override
    protected Map<String, Object> getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("description", description);
        map.put("createDate", Utils.simpleDate(createDate));
        map.put("updateDate", Utils.simpleDate(updateDate));
        return map;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getCreateDate() {
        return createDate;
    }

    public Integer getUpdateDate() {
        return updateDate;
    }
}
