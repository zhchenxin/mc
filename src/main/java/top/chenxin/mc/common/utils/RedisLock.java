package top.chenxin.mc.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisLock {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 获取锁
     * @param key 锁名称
     * @param expire 超时时间 单位秒
     * @return 如果获取到锁, 赶回true, 没有则返回false
     */
    public boolean checkLock(String key, int expire) {
        int now = Utils.getCurrentTimestamp();
        int expireTimestamp = now + expire;
        boolean res = stringRedisTemplate.opsForValue().setIfAbsent(key, expireTimestamp+"");
        if (res) {
            return true;
        }

        String getVal = stringRedisTemplate.opsForValue().get(key);
        if (getVal == null) {
            return false;
        }
        int getTimestamp = Integer.valueOf(getVal);
        if (getTimestamp > now) {
            return false;
        }

        int getSetTimestamp = Integer.valueOf(stringRedisTemplate.opsForValue().getAndSet(key, expireTimestamp + ""));

        return getTimestamp == getSetTimestamp;
    }

    /**
     * 释放锁
     * @param key 锁名称
     */
    public void deleteLock(String key) {
        Integer now = Utils.getCurrentTimestamp();
        Integer getTimestamp = Integer.valueOf(stringRedisTemplate.opsForValue().get(key));
        if (getTimestamp > now) {
            stringRedisTemplate.delete(key);
        }
    }

    

}
