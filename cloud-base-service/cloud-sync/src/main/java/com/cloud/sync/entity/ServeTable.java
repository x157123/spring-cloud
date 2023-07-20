package com.cloud.sync.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.common.core.entity.BaseEntity;
import lombok.Data;

/**
 * @author liulei
 * 同步表
 */
@Data
@TableName("sync_serve_table")
public class ServeTable extends BaseEntity {

    /**
     * 采集任务Id
     */
    private Long serveId;

    /**
     * 数据表映射
     */
    private Long tableMapId;

    /**
     * 版本
     */
    private Integer version;
}
