package top.chenxin.mc.web.response.cron;

import top.chenxin.mc.entity.Cron;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.web.response.AbstractResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateResponse extends AbstractResponse {

    private Cron cron;

    public UpdateResponse(Cron cron) {
        this.cron = cron;
    }

    @Override
    protected Map getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("cron", getCron());
        return map;
    }

    private Map getCron() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", cron.getId());
        map.put("name", cron.getName());
        map.put("description", cron.getDescription());
        map.put("spec", cron.getSpec());
        return map;
    }
}
