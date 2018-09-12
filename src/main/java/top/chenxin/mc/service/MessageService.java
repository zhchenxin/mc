package top.chenxin.mc.service;

import top.chenxin.mc.response.Response;
import top.chenxin.mc.request.message.ListForm;

public interface MessageService {
    Response search(ListForm listForm);

    Response getDetailById(Long id);
}
