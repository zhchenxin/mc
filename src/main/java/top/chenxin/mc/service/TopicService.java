package top.chenxin.mc.service;

import top.chenxin.mc.form.topic.CreateForm;
import top.chenxin.mc.form.topic.ListForm;
import top.chenxin.mc.form.topic.PushForm;
import top.chenxin.mc.resource.TopicCollection;

public interface TopicService {

    void create(CreateForm createForm);

    TopicCollection search(ListForm listForm);

    void push(PushForm form);
}
