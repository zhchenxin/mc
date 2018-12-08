package top.chenxin.mc.web.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.chenxin.mc.service.CronService;
import top.chenxin.mc.web.APIResponse;
import top.chenxin.mc.web.request.cron.CreateForm;
import top.chenxin.mc.web.request.cron.ListForm;
import top.chenxin.mc.web.request.cron.UpdateForm;

@RestController
@RequestMapping("")
public class CronController extends BaseController {
    @Autowired
    private CronService cronService;

    @RequestMapping(value = "cron", method = RequestMethod.GET)
    private ResponseEntity<JSONObject> index(@Validated ListForm form) {
        return APIResponse.success(cronService.getPage(form.getTopicId(), form.getPage(), form.getLimit()));
    }

    @RequestMapping(value = "cron", method = RequestMethod.POST)
    private ResponseEntity<JSONObject> create(@Validated CreateForm form) {
        cronService.insert(form.getName(), form.getDescription(), form.getSpec(), form.getTopicId());
        return APIResponse.success();
    }

    @RequestMapping(value = "cron/{id}", method = RequestMethod.PUT)
    private ResponseEntity<JSONObject> update(@PathVariable("id") Long id, @Validated UpdateForm form) {
        cronService.update(id, form.getName(), form.getDescription(), form.getSpec());
        return APIResponse.success();
    }

    @RequestMapping(value = "cron/{id}", method = RequestMethod.DELETE)
    private ResponseEntity<JSONObject> delete(@PathVariable("id") Long id) {
        cronService.delete(id);
        return APIResponse.success();
    }

    @RequestMapping(value = "cron/{id}/start", method = RequestMethod.PUT)
    private ResponseEntity<JSONObject> start(@PathVariable("id") Long id) {
        cronService.start(id);
        return APIResponse.success();
    }

    @RequestMapping(value = "cron/{id}/stop", method = RequestMethod.PUT)
    private ResponseEntity<JSONObject> stop(@PathVariable("id") Long id) {
        cronService.stop(id);
        return APIResponse.success();
    }
}
