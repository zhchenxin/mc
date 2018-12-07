package top.chenxin.mc.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class IdBuilder {

    @Autowired
    RedisPool redisPool;

    private static final String idKey  = "message.id.counter";

    public Long createMessageId() {
        try (Jedis redis = redisPool.getClient()) {
            return redis.incr(idKey);
        }
    }
}
