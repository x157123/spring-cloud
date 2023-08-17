package com.cloud.sync.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.common.core.entity.BaseEntity;
import lombok.Data;

/**
 * @author liulei
 */
@Data
@TableName("sync_table_associate")
public class TableAssociate extends BaseEntity {

    /**
     * 服务Id
     */
    private Long serveId;
    
    /**
     * 服务名称
     */
    private String name;

    /**
     * 读取表
     */
    private Long readTableId;

    /**
     * 写入表
     */
    private Long writeTableId;
}
