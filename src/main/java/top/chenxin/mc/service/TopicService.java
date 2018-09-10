package top.chenxin.mc.service;

import top.chenxin.mc.form.topic.CreateForm;
import top.chenxin.mc.form.topic.ListForm;
import top.chenxin.mc.form.topic.PushForm;
import top.chenxin.mc.response.topic.ListResponse;

public interface TopicService {

    void create(CreateForm createForm);

    ListResponse search(ListForm listForm);

    void push(PushForm form);
}
