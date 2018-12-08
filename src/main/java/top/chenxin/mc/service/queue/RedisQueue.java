package top.chenxin.mc.service.queue;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import top.chenxin.mc.common.utils.RedisPool;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.service.model.MessageModel;


/**
 * 数据库队列
 */
@Service
public class RedisQueue implements Queue {

    @Autowired
    RedisPool redisPool;

    // 默认队列名称
    private static final String DefaultQueue = "queue:default";

    // 最大超时时间
    private static final int MaxTimeout = 600;

    @Override
    public MessageModel pop() {
        String str = _pop(DefaultQueue, MaxTimeout);
        if (str == null) {
            return null;
        }
        MessageModel model = str2Model(str);
        model.setReserved(str);
        return model;
    }

    @Override
    public void push(MessageModel message) {
        _push(DefaultQueue, model2Str(message), message.getDelay());
    }

    @Override
    public void migrate() {
        _migrateExpiredJobs(DefaultQueue);
    }

    @Override
    public void success(MessageModel message) {
        _delete(DefaultQueue, message.getReserved());
    }

    @Override
    public void failed(MessageModel message) {
        if (needRetry(message)) {
            // 重新放入数据库中
            _release(DefaultQueue, message.getReserved());
        } else {
            // 删除消息
            _delete(DefaultQueue, message.getReserved());
        }
    }

    private String model2Str(MessageModel message) {
        return JSON.toJSONString(message);
    }

    private MessageModel str2Model(String message) {
        return JSON.parseObject(message, MessageModel.class);
    }

    /**
     * 向队列中推送数据
     * @param queue   队列名称
     * @param message 消息内容
     * @param delay   延迟时间
     */
    private void _push(String queue, String message, int delay) {
        try (Jedis client = redisPool.getClient()){
            if (delay == 0) {
                client.rpush(queue, message);
            } else {
                int score = Utils.getCurrentTimestamp() + delay;
                client.zadd(queue + ":delayed", score, message);
            }
        }
    }

    /**
     * 从队列中弹出消息
     * @param queue   队列名称
     * @param timeout 任务超时时间
     * @return 消息内容
     */
    private String _pop(String queue, Integer timeout) {
        try (Jedis client = redisPool.getClient()){
            String lua =
                    "local job = redis.call('lpop', KEYS[1])\n" +
                    "local reserved = false\n" +
                    "if(job ~= false) then\n" +
                    "    reserved = cjson.decode(job)\n" +
                    "    reserved['attempts'] = reserved['attempts'] + 1\n" +
                    "    reserved = cjson.encode(reserved)\n" +
                    "    redis.call('zadd', KEYS[2], ARGV[1], reserved)\n" +
                    "end\n" +
                    "return reserved";

            Object ret = client.eval(lua, 2, queue, queue + ":reserved",
                    "" + Utils.getCurrentTimestamp() + timeout*2);
            if (ret == null) {
                return null;
            }
            return ret.toString();
        }
    }

    /**
     * 将保留队列中的消息删除
     * @param queue   队列名
     * @param message 消息内容
     */
    private void _delete(String queue, String message) {
        try (Jedis client = redisPool.getClient()){
            client.zrem(queue + ":reserved", message);
        }
    }

    /**
     * 将超时的任务和延迟任务重新放入消息队列中
     * @param queue 任务名称
     */
    private void _migrateExpiredJobs(String queue) {
        try (Jedis client = redisPool.getClient()){
            String lua =
                    "local val = redis.call('zrangebyscore', KEYS[1], '-inf', ARGV[1])\n" +
                    "if(next(val) ~= nil) then\n" +
                    "    redis.call('zremrangebyrank', KEYS[1], 0, #val - 1)\n" +
                    "    for i = 1, #val, 100 do\n" +
                    "        redis.call('rpush', KEYS[2], unpack(val, i, math.min(i+99, #val)))\n" +
                    "    end\n" +
                    "end\n" +
                    "return val";

            client.eval(lua, 2, queue + ":delayed", queue, "" + Utils.getCurrentTimestamp());
            client.eval(lua, 2, queue + ":reserved", queue, "" + Utils.getCurrentTimestamp());
        }
    }

    /**
     * 将保留消息重新放入消息队列中，从而重试
     * @param queue      队列名称
     * @param message    消息内容
     */
    private void _release(String queue, String message) {
        try (Jedis client = redisPool.getClient()){
            Transaction multi = client.multi();
            multi.zrem(queue + ":reserved", message);
            multi.rpush(queue, message);
            multi.exec();
        }
    }
}
