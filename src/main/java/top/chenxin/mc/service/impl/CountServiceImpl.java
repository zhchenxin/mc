package top.chenxin.mc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.chenxin.mc.common.utils.RedisCount;
import top.chenxin.mc.common.utils.Utils;
import top.chenxin.mc.dao.MessageMinCountDao;
import top.chenxin.mc.entity.MessageMinCount;
import top.chenxin.mc.service.CountService;

@Service
public class CountServiceImpl implements CountService {

    @Autowired
    RedisCount redisCount;

    @Autowired
    MessageMinCountDao messageMinCountDao;

    @Override
    @Transactional
    public void gather(int timestamp) {
        Long popNum = redisCount.getPopNum(timestamp);
        Long pushNum = redisCount.getPushNum(timestamp);

        MessageMinCount count = new MessageMinCount();
        count.setType(MessageMinCount.TypePush);
        count.setNum(pushNum);
        count.setCreateDate(Utils.getCurrentTimestamp());
        messageMinCountDao.insert(count);

        count = new MessageMinCount();
        count.setType(MessageMinCount.TypePop);
        count.setNum(popNum);
        count.setCreateDate(Utils.getCurrentTimestamp());
        messageMinCountDao.insert(count);

    }
}
