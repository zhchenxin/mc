package top.chenxin.mc.resource;

import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.core.Resource;
import top.chenxin.mc.entity.Cron;

import java.util.HashMap;
import java.util.Map;

public class CronResource extends Resource {

    private Long topicId;

    private Long id;
    private String name;
    private String description;
    private String spec;
    private Integer createDate;
    private Integer updateDate;
    private Integer status;

    public CronResource(Cron cron) {
        this.topicId = cron.getTopicId();

        this.id = cron.getId();
        this.name = cron.getName();
        this.description = cron.getDescription();
        this.spec = cron.getSpec();
        this.createDate = cron.getCreateDate();
        this.updateDate = cron.getUpdateDate();
        this.status = cron.getStatus();
    }

    @Override
    protected Map<String, Object> getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("description", description);
        map.put("spec", spec);
        map.put("createDate", Utils.simpleDate(createDate));
        map.put("updateDate", Utils.simpleDate(updateDate));
        map.put("status", Utils.simpleDate(status));

        map.put("topicId", topicId);
        return map;
    }
}
