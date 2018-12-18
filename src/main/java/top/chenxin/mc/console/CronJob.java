package top.chenxin.mc.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Component;
import top.chenxin.mc.common.utils.RedisLock;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.entity.Cron;
import top.chenxin.mc.service.CountService;
import top.chenxin.mc.service.CronService;
import top.chenxin.mc.service.MessageService;
import top.chenxin.mc.service.TopicService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class CronJob {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CronService cronService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private MessageService messageService;

    @Autowired
    private CountService countService;

    /**
     * 每分钟跑一次，检查数据库中定时任务是否需要执行
     */
    @Scheduled(cron = "0 * * * * *")
    protected void cron() {
        String lockKey = "cron";
        String requestId = Utils.getRandomString(32);
        if (redisLock.lock(lockKey, requestId, 30)) {
            return;
        }
        List<Cron> cronList = cronService.getAllNormalCron();
        for (Cron cron : cronList) {
            if (specRun(cron.getSpec())) {
                try {
                    messageService.push(cron.getTopicId(), "", 0);
                } catch (Exception e) {
                    logger.error("定时任务失败,原因:" + e.getMessage(), e);
                }
            }
        }

        redisLock.unLock(lockKey, requestId);
    }

    /**
     * 每分钟跑一次，插入统计数据
     */
    @Scheduled(cron = "0 * * * * *")
    protected void minCount() {
        String lockKey = "min_count";
        String requestId = Utils.getRandomString(32);
        if (redisLock.lock(lockKey, requestId, 30)) {
            return;
        }

        countService.gather(Utils.getCurrentTimestamp() - 30);

        redisLock.unLock(lockKey, requestId);
    }

    /**
     * 每秒跑一次，将数据库中延迟执行的消息推送到队列中
     */
    @Scheduled(cron = "* * * * * *")
    protected void migrate() {
        String lockKey = "migrate";
        String requestId = Utils.getRandomString(32);
        if (redisLock.lock(lockKey, requestId, 60)) {
            return;
        }
        try {
            messageService.migrate();
        } catch (Exception e) {
            logger.error("合并失败,原因:" + e.getMessage(), e);
        }

        redisLock.unLock(lockKey, requestId);
    }

    /**
     * 判断当前时间指定的 spec 是否能够执行
     */
    private boolean specRun(String spec) {

        int currentTimestamp = Utils.getCurrentTimestamp();

        CronTrigger trigger = new CronTrigger("0 " + spec);
        SimpleTriggerContext context = new SimpleTriggerContext();
        context.update(null, null, new Date(((long)currentTimestamp - 60)*1000));
        Date date = trigger.nextExecutionTime(context);
        assert date != null;

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        return dateFormat.format(date).equalsIgnoreCase(dateFormat.format(new Date(currentTimestamp * 1000L)));
    }
}
