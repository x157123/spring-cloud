package com.cloud.auto.code.mysql;

import lombok.Data;

/**
 * @author liulei
 * 中间关联表信息
 */
@Data
public class MysqlMergeTable {
    
    /**
     * 主要维护关联表
     */
    private String maintain;

    /**
     * 关联表
     */
    private String leftTable;

    private String leftTableClass;

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
    private String leftMergeTableColumnClass;

    /**
     * 关联列
     */
    private String rightMergeTableColumn;
    private String rightMergeTableColumnClass;

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
