package com.cloud.sync.service;

import java.util.List;

public interface SyncService {

    /**
     * 启动
     */
    void begin(Long connectId);

    /**
     * 开启服务
     */
    void start(Long connectId);

    /**
     * 停止服务
     */
    void stop(Long connectId);

    /**
     * 获取信息
     */
    void msg();

    void writeData(Long connectId, String table, String type, List<String> data);
}
