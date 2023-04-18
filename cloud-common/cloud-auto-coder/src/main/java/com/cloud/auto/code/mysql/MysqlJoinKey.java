package com.cloud.auto.code.mysql;

import com.cloud.auto.code.util.StringUtil;
import lombok.Data;

import java.util.List;

/**
 * @author liulei
 */
@Data
public class MysqlJoinKey {

    /**
     * 原始表
     */
    private String tableName;

    /**
     * 外键关联字段
     */
    private String columnName;

    /**
     * 关联表表名
     */
    private String tableComment;

    private String tableNameClass;

    private String columnNameClass;

    public MysqlJoinKey(String tableName, String columnName, String tableComment, List<String> prefix) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.tableComment = tableComment;
        this.tableNameClass = StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(tableName, prefix));
        this.columnNameClass = StringUtil.toUpperCaseFirstOne(StringUtil.getClassName(columnName));
    }
}
