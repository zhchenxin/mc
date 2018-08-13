package top.zhchenxin.mc.service;

import top.zhchenxin.mc.form.topic.CreateForm;
import top.zhchenxin.mc.form.topic.ListForm;
import top.zhchenxin.mc.form.topic.PushForm;
import top.zhchenxin.mc.resource.TopicCollection;

public interface TopicService {

    void create(CreateForm createForm);

    TopicCollection search(ListForm listForm);

    void push(PushForm form);
}
