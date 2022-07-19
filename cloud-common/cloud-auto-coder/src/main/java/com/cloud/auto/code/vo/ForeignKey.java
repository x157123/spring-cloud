package com.cloud.auto.code.vo;

import com.cloud.auto.code.util.StringUtil;
import com.cloud.common.core.utils.BeanUtil;
import lombok.Data;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author liulei
 * select table_name,column_name,REFERENCED_TABLE_NAME as referenced_table_name,REFERENCED_COLUMN_NAME as referenced_column_name
 * from INFORMATION_SCHEMA.KEY_COLUMN_USAGE
 * where REFERENCED_TABLE_NAME='user' and constraint_schema = 'testuser';
 */
@Data
public class ForeignKey {

    /**
     * 原始表
     */
    private String tableName;

    /**
     * 外键关联字段
     */
    private String columnName;

    /**
     * 外键关联表
     */
    private String referencedTableName;

    /**
     * 外键关联表字段
     */
    private String referencedColumnName;

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

    /**
     * 表前缀
     */
    private List<String> prefix;


    public String getPackagePath(){
        return StringUtil.getString(packagePath);
    }

    public String getTableNameClass() {
        String className = tableName;
        if (prefix != null && prefix.size() > 0) {
            for (String pre : prefix) {
                int i = className.indexOf(pre);
                if(i == 0){
                    className = className.substring(pre.length());
                }
            }
        }
        return StringUtil.dbToClassName(className);
    }

    public String getColumnNameClass() {
        return StringUtil.dbToClassName(columnName);
    }

    public String getReferencedTableNameClass() {
        String className = referencedTableName;
        if (prefix != null && prefix.size() > 0) {
            for (String pre : prefix) {
                int i = className.indexOf(pre);
                if(i == 0){
                    className = className.substring(pre.length());
                }
            }
        }
        return StringUtil.dbToClassName(className);
    }

    public String getReferencedColumnNameClass() {
        return StringUtil.dbToClassName(referencedColumnName);
    }



    public String getComment() {
        return BeanUtil.replaceBlank(comment);
    }
}
