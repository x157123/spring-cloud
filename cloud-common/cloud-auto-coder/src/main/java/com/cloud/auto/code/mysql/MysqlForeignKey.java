package com.cloud.auto.code.mysql;

import com.cloud.auto.code.util.StringUtil;
import lombok.Data;

import java.util.List;


@Data
public class MysqlForeignKey {

    /**
     * 原始表
     */
    private String tableName;
    private String tableNameClass;

    /**
     * 外键关联字段
     */
    private String columnName;
    private String columnNameClass;

    /**
     * 外键关联表
     */
    private String joinTableName;
    private String joinTableNameClass;

    /**
     * 外键关联表字段
     */
    private String joinColumnName;
    private String joinColumnNameClass;

    /**
     * 特殊包路径
     */
    private String packagePath;

    /**
     * 列表中文名称
     */
    private String comment;

    /**
     * 是否唯一
     */
    private boolean uni;

    public MysqlForeignKey(String tableName, String columnName, String joinTableName, String joinColumnName, List<String> prefix, String packagePath, String comment, boolean uni) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.joinTableName = joinTableName;
        this.joinColumnName = joinColumnName;
        this.tableNameClass = StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(tableName, prefix));
        this.columnNameClass = StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(columnName));
        this.joinTableNameClass = StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(joinTableName, prefix));
        this.joinColumnNameClass = StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(joinColumnName));
        this.packagePath = packagePath;
        this.comment = comment;
        this.uni = uni;
    }

}
