package com.cloud.auto.code.mysql;

import lombok.Data;

/**
 * @author liulei
 * 中间关联表信息
 */
@Data
public class MysqlMergeTable {

    /**
     * 关联表
     */
    private String leftTable;

    /**
     * 关联列
     */
    private String leftTableColumn;

    /**
     * 关联中间表
     */
    private String mergeTable;

    /**
     * 关联列
     */
    private String leftMergeTableColumn;

    /**
     * 关联列
     */
    private String rightMergeTableColumn;

    /**
     * 关联表
     */
    private String rightTable;

    /**
     * 关联列
     */
    private String rightTableColumn;

    /**
     * 关联表
     */
    private String rightTableClass;

    /**
     * 关联列
     */
    private String rightTableColumnClass;

    /**
     * 说明
     */
    private String comment;

    /**
     * 扩展包路径
     */
    private String packagePath;

    /**
     * 表java类名称
     */
    private String tableNameClass;
}
