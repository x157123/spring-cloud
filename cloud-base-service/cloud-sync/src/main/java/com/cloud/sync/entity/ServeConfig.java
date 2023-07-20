package com.cloud.sync.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.common.core.entity.BaseEntity;
import lombok.Data;

/**
 * @author liulei
 * 同步启动服务
 */
@Data
@TableName("sync_serve_config")
public class ServeConfig extends BaseEntity {

    /**
     * 采集数据库Id
     */
    private Long readConnectId;

    /**
     * 状态0未启动，1停用中，5待启动，10启动
     */
    private Integer state;

    /**
     * 数据库采集偏移情况
     */
    private String offSet;

    /**
     * 版本
     */
    private Integer version;
}
