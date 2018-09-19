package top.chenxin.mc.service.impl;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import top.chenxin.mc.dao.CronDao;
import top.chenxin.mc.entity.Cron;
import top.chenxin.mc.service.CronService;
import top.chenxin.mc.service.exception.ServiceException;

import java.util.List;

@Service
public class CronServiceImpl implements CronService {

    @Autowired
    private CronDao cronDao;

    @Override
    public void insert(String name, String description, String spec, Long topicId) {
        this.checkSpec(spec);

        Cron cron = new Cron();
        cron.setName(name);
        cron.setDescription(description);
        cron.setSpec(spec);
        cron.setTopicId(topicId);
        cron.setStatus(Cron.StatusNormal);
        cronDao.insert(cron);
    }

    @Override
    public void update(Long id, String name, String description, String spec) {
        this.checkSpec(spec);
        Cron cron = new Cron();
        cron.setId(id);
        cron.setName(name);
        cron.setDescription(description);
        cron.setSpec(spec);
        cronDao.update(cron);
    }

    @Override
    public void delete(Long id) {
        cronDao.delete(id);
    }

    @Override
    public void start(Long id) {
        Cron cron = new Cron();
        cron.setId(id);
        cron.setStatus(Cron.StatusNormal);
        cronDao.update(cron);
    }

    @Override
    public void stop(Long id) {
        Cron cron = new Cron();
        cron.setId(id);
        cron.setStatus(Cron.StatusStop);
        cronDao.update(cron);
    }

    @Override
    public Page<Cron> search(Long topicId, Integer page, Integer limit) {
        return cronDao.search(topicId, page, limit);
    }

    @Override
    public List<Cron> getAll() {
        return cronDao.getAll();
    }

    @Override
    public Cron getById(Long id) {
        return cronDao.getById(id);
    }

    private void checkSpec(String spec) {
        try {
            new CronTrigger("0 " + spec);
        } catch (Exception exception) {
            throw new ServiceException("spce 参数格式错误");
        }
    }
}
