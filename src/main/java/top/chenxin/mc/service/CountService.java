package top.chenxin.mc.service;


public interface CountService {

    /**
     * 收集某段时间的统计信息，例如收集 10:20 分的统计信息，传 10:20 分之内任意秒的时间戳
     * @param timestamp 时间戳
     */
    void gather(int timestamp);
}
