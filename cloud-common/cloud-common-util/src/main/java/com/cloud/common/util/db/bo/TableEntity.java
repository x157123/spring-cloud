package com.cloud.common.util.db.bo;

import lombok.Data;

import java.util.List;

/**
 * @author liulei
 */
@Data
public class TableEntity {
    /**
     * 名称
     */
    private String tableName;
    /**
     * 备注
     */
    private String comments;
    /**
     * 列名
     */
    private List<ColumnEntity> columns;

    /**
     * 功能说明
     */
    private String description;
}
