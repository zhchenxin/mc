package top.chenxin.mc.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Redis 统计使用的键名
 */
@Component
public class RedisCount {

    @Autowired
    private RedisPool redisPool;

    private final static DateFormat DateFormat = new SimpleDateFormat("yyyyMMddHHmm");;


    /**
     * 每分钟消息数
     */
    public void incrPushNum() {
        Date date = new Date();
        String key = getPushNum(DateFormat.format(date));
        try (Jedis redis = redisPool.getClient()) {
            Transaction multi = redis.multi();
            multi.incr(key);
            multi.expire(key, 600);
            multi.exec();
        }
    }

    public long getPushNum(int timestamp) {
        Date date = new Date(timestamp * 1000L);
        String key = getPushNum(DateFormat.format(date));
        try (Jedis redis = redisPool.getClient()) {
            String num = redis.get(key);
            if (num == null) return 0;
            return Long.parseLong(num);
        }
    }

    /**
     * 每分钟消费数
     */
    public void incrPopNum() {
        Date date = new Date();
        String key = getPopNum(DateFormat.format(date));
        try (Jedis redis = redisPool.getClient()) {
            Transaction multi = redis.multi();
            multi.incr(key);
            multi.expire(key, 600);
            multi.exec();
        }
    }

    public long getPopNum(int timestamp) {
        Date date = new Date(timestamp * 1000L);
        String key = getPushNum(DateFormat.format(date));
        try (Jedis redis = redisPool.getClient()) {
            String num = redis.get(key);
            if (num == null) return 0;
            return Long.parseLong(num);
        }
    }

    // 获取每分钟增加的消息数
    private String getPushNum(String min) {
        return "count:push:" + min;
    }

    // 获取每秒消费的消息数
    private String getPopNum(String min) {
        return "count:pop:" + min;
    }
}
