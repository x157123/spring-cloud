package com.cloud.sync.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.common.core.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liulei
 * 同步数据库列配置
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sync_column_config")
public class ColumnConfig extends BaseEntity {

    /**
     * 表Id
     */
    private Long tableId;

    /**
     * 服务Id
     */
    private Long serveId;

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

    /**
     * 数据加工
     */
    private String convertFun;
}
