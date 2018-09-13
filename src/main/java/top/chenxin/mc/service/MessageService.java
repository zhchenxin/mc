package top.chenxin.mc.service;


import top.chenxin.mc.service.dto.MessageDetail;
import top.chenxin.mc.service.dto.MessageSearchList;

public interface MessageService {
    /**
     * 按照条件搜索
     * @param customerId 默认值0
     * @param status 默认值0
     */
    MessageSearchList search(Long customerId, Integer status, Integer page, Integer limit);

    MessageDetail getDetailById(Long id);
}
