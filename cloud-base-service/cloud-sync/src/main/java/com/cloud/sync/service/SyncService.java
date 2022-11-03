package com.cloud.sync.service;

public interface SyncService {

    /**
     * 启动
     */
    void begin();

    /**
     * 开启服务
     */
    void start() ;

    /**
     * 停止服务
     */
    void stop();

    /**
     * 获取信息
     */
    void msg() ;
}
