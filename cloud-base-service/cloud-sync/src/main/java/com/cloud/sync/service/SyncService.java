package com.cloud.sync.service;

import com.cloud.sync.param.SyncConfigParam;

import java.util.List;
import java.util.Map;

public interface SyncService {

    /**
     * 启动
     */
    void begin(Long connectId);

    /**
     * 停止服务
     */
    void stop(Long connectId);

    /**
     * 获取信息
     */
    void msg();

    /**
     * 写入数据
     *
     * @param map
     */
    void writeData(Map<String, List<String>> map);

    /**
     * 保存 数据
     *
     * @param syncConfig
     */
    void saveSyncConfig(SyncConfigParam syncConfig);
}
