package com.cloud.auto.code.vo;

import com.cloud.auto.code.util.StringUtil;
import lombok.Data;

/**
 * @author liulei
 */
@Data
public class Key {

    /**
     * 原始表
     */
    private String tableName;

    /**
     * 外键关联字段
     */
    private String columnName;

    public String getTableNameClass() {
        return StringUtil.dbToClassName(tableName);
    }

    public String getColumnNameClass() {
        return StringUtil.dbToClassName(columnName);
    }
}
