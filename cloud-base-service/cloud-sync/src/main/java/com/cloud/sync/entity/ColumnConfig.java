package com.cloud.sync.entity;

import com.cloud.common.core.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
* @author liulei
* 同步数据库列配置
*/
@Data
@TableName("sync_column_config")
public class ColumnConfig extends BaseEntity {

    /**
    * 表Id
    */
    private Long tableId;

    /**
    * 顺序对应
    */
    private Integer seq;

    /**
    * 表明
    */
    private String columnName;

    /**
    * 备注
    */
    private String columnRemark;

    /**
    * 数据类型
    */
    private String columnType;

    /**
    * 主键
    */
    private Integer columnPrimaryKey;

    /**
    * 默认值
    */
    private String def;
}
