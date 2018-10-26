package top.chenxin.mc.common.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedisLockTest implements Runnable, InitializingBean {

    @Autowired
    RedisLock redisLock;

    private static int val;

    private List<Thread> threadList;

    @Override
    public void afterPropertiesSet() {

        threadList = new ArrayList<>();

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);

        for (int i = 0; i < 10000; i++) {
            fixedThreadPool.execute(this);
        }
    }

    @Override
    public void run() {
        String lockKey = "test_key";
        String reqestId = Utils.getRandomString(32);
        while (!redisLock.checkLock(lockKey, reqestId,10)) {

        }
        val++;
        System.out.println(val);
        redisLock.deleteLock(lockKey, reqestId);
    }
}
