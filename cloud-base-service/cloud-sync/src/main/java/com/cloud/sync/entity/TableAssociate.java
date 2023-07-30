package com.cloud.sync.entity;

import com.cloud.common.core.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
* @author liulei
* 
*/
@Data
@TableName("sync_table_associate")
public class TableAssociate extends BaseEntity {

    /**
    * 服务Id
    */
    private Long serveId;

    /**
    * 读取表
    */
    private Long readTableId;

    /**
    * 写入表
    */
    private Long writeTableId;
}
