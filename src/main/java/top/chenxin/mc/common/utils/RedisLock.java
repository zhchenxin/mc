package top.chenxin.mc.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Collections;

@Component
public class RedisLock {

    @Autowired
    private RedisPool redisPool;

    /**
     * 获取锁
     * @param key 锁名称
     * @param requestId 请求id
     * @param expire 超时时间 单位秒
     * @return 如果获取到锁, 赶回true, 没有则返回false
     */
    public boolean checkLock(String key, String requestId, int expire) {
        try (Jedis redis = redisPool.getClient()) {
            String res = redis.set(key, requestId, "NX", "PX", expire);
            if (res != null && res.equalsIgnoreCase("ok")) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 释放锁
     * @param key 锁名称
     */
    public void deleteLock(String key, String requestId) {
        try (Jedis redis = redisPool.getClient()) {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            redis.eval(script, Collections.singletonList(key), Collections.singletonList(requestId));
        }
    }

    

}
