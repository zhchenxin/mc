package top.chenxin.mc.web.controller;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.entity.Cron;
import top.chenxin.mc.entity.Topic;
import top.chenxin.mc.service.CronService;
import top.chenxin.mc.service.TopicService;
import top.chenxin.mc.web.request.cron.CreateForm;
import top.chenxin.mc.web.request.cron.ListForm;
import top.chenxin.mc.web.request.cron.UpdateForm;
import top.chenxin.mc.web.response.SuccessResponse;
import top.chenxin.mc.web.response.cron.ListResponse;
import top.chenxin.mc.web.response.cron.UpdateResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cron")
public class CronController extends BaseController {
    @Autowired
    private CronService cronService;

    @Autowired
    private TopicService topicService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private Map index(@Validated ListForm form) {
        Page<Cron> cronPage = cronService.search(form.getTopicId(), form.getPage(), form.getLimit());

        List<Long> topicIds = cronPage.stream().map(Cron::getTopicId).collect(Collectors.toList());
        Utils.removeDuplicate(topicIds);

        List<Topic> topicList = topicService.getByIds(topicIds);
        return new ListResponse(cronPage, topicList).toMap();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    private Map create(@Validated CreateForm form) {
        cronService.insert(form.getName(), form.getDescription(), form.getSpec(), form.getTopicId());
        return new SuccessResponse().toMap();
    }

    @RequestMapping(value = "update", method = RequestMethod.GET)
    private Map update(@RequestParam("id") Long id) {
        Cron cron = cronService.getById(id);
        return new UpdateResponse(cron).toMap();
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    private Map update(@RequestParam("id") Long id, @Validated UpdateForm form) {
        cronService.update(id, form.getName(), form.getDescription(), form.getSpce());
        return new SuccessResponse().toMap();
    }

    @RequestMapping(value = "start", method = RequestMethod.POST)
    private Map start(@RequestParam("id") Long id) {
        cronService.start(id);
        return new SuccessResponse().toMap();
    }

    @RequestMapping(value = "stop", method = RequestMethod.POST)
    private Map stop(@RequestParam("id") Long id) {
        cronService.stop(id);
        return new SuccessResponse().toMap();
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    private Map delete(@RequestParam("id") Long id) {
        cronService.delete(id);
        return new SuccessResponse().toMap();
    }
}
