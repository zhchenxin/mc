package top.chenxin.mc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.chenxin.mc.service.CronService;
import top.chenxin.mc.request.cron.CreateForm;
import top.chenxin.mc.request.cron.ListForm;
import top.chenxin.mc.request.cron.UpdateForm;

import java.util.Map;

@RestController
@RequestMapping("/cron")
public class CronController extends BaseController {
    @Autowired
    private CronService cronService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private Map index(@Validated ListForm form) {
        return cronService.getList(form.getTopicId(), form.getPage(), form.getLimit()).toMap();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    private Map create(@Validated CreateForm form) {
        cronService.insert(form.getName(), form.getDescription(), form.getSpec(), form.getTopicId());
        return success();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    private Map update(@PathVariable("id") Long id, @Validated UpdateForm form) {
        cronService.update(id, form.getName(), form.getDescription(), form.getSpec());
        return success();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    private Map delete(@PathVariable("id") Long id) {
        cronService.delete(id);
        return success();
    }

    @RequestMapping(value = "{id}/start", method = RequestMethod.PUT)
    private Map start(@PathVariable("id") Long id) {
        cronService.start(id);
        return success();
    }

    @RequestMapping(value = "{id}/stop", method = RequestMethod.PUT)
    private Map stop(@PathVariable("id") Long id) {
        cronService.stop(id);
        return success();
    }
}
