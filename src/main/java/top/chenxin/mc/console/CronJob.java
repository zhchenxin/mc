package top.chenxin.mc.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Component;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.entity.Cron;
import top.chenxin.mc.service.CronService;
import top.chenxin.mc.service.TopicService;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
public class CronJob {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CronService cronService;

    @Autowired
    private TopicService topicService;

    @Scheduled(cron = "0 * * * * *")
    protected void runEverySecond() {
        List<Cron> cronList = cronService.getAllNormalCron();
        for (Cron cron : cronList) {
            if (specRun(cron.getSpec())) {
                try {
                    topicService.push(cron.getTopicId(), "", 0);
                } catch (Exception e) {
                    logger.error("定时任务发布失败,原因:" + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 判断当前时间指定的 spec 是否能够执行
     */
    private boolean specRun(String spec) {

        int currentTimestamp = Utils.getCurrentTimestamp();

        CronTrigger trigger = new CronTrigger("0 " + spec);
        SimpleTriggerContext context = new SimpleTriggerContext();
        context.update(null, null, new Date(((long)currentTimestamp - 1)*1000));
        Date date = trigger.nextExecutionTime(context);
        assert date != null;
        return (int)(date.getTime() / 1000) == currentTimestamp;
    }
}
