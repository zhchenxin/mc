package top.chenxin.mc.common.utils;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class RedisPool implements InitializingBean {
    @Value("${redis.host}")
    private String host = "localhost";

    private JedisPool jedisPool;

    @Override
    public void afterPropertiesSet() {
        //连接池配置
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        // 最大连接数
        poolConfig.setMaxTotal(100);
        poolConfig.setMaxIdle(100);
        poolConfig.setMinIdle(2);
        poolConfig.setMaxWaitMillis(3000);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setTimeBetweenEvictionRunsMillis(3000);

        jedisPool = new JedisPool(poolConfig,host);
    }

    public Jedis getClient() {
        return jedisPool.getResource();
    }
}
