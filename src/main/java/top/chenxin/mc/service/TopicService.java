package top.chenxin.mc.service;

import top.chenxin.mc.request.topic.CreateForm;
import top.chenxin.mc.request.topic.ListForm;
import top.chenxin.mc.request.topic.PushForm;
import top.chenxin.mc.response.topic.ListResponse;

public interface TopicService {

    void create(CreateForm createForm);

    ListResponse search(ListForm listForm);

    void push(PushForm form);
}
