package top.chenxin.mc.web.response.cron;

import com.github.pagehelper.Page;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.entity.Cron;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.web.response.PaginationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListResponse extends PaginationResponse {

    private Page<Cron> cronPage;

    private Map<Long, Topic> topicMap;

    public ListResponse(Page<Cron> cronPage, List<Topic> topicList) {
        this.cronPage = cronPage;
        this.setPage(cronPage);

        this.topicMap = new HashMap<>();
        for (Topic topic : topicList) {
            this.topicMap.put(topic.getId(), topic);
        }
    }

    protected List getList() {
        List<Map<String, Object>> list = new ArrayList<>();

        for (Cron cron : cronPage) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", cron.getId());
            map.put("name", cron.getName());
            map.put("description", cron.getDescription());
            map.put("spec", cron.getSpec());
            map.put("createDate", Utils.simpleDate(cron.getCreateDate()));

            map.put("topicName", getTopic(cron).getName());
            list.add(map);
        }

        return list;
    }

    private Topic getTopic(Cron cron) {
        return this.topicMap.get(cron.getTopicId());
    }
}
