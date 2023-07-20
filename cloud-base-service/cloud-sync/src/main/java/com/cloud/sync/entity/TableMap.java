package com.cloud.sync.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.common.core.entity.BaseEntity;
import lombok.Data;

/**
 * @author liulei
 * 表映射
 */
@Data
@TableName("sync_table_map")
public class TableMap extends BaseEntity {

    /**
     * 采集数据Id
     */
    private Long readConnectId;

    /**
     * 读取表Id
     */
    private Long readTableId;

    /**
     * 写入数据库Id
     */
    private Long writeConnectId;

    /**
     * 写入表Id
     */
    private Long writeTableId;

    /**
     * 版本
     */
    private Integer version;
}
