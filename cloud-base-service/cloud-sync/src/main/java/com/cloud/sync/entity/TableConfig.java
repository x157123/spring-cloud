package com.cloud.sync.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.common.core.entity.BaseEntity;
import lombok.Data;

/**
 * @author liulei
 * 同步表配置
 */
@Data
@TableName("sync_table_config")
public class TableConfig extends BaseEntity {

    /**
     * 数据库id
     */
    private Long connectId;

    /**
     * 数据库表
     */
    private String tableName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 版本
     */
    private Integer version;
}
