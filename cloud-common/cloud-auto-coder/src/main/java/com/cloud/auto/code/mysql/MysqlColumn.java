package com.cloud.auto.code.mysql;

import com.cloud.auto.code.util.StringUtil;
import com.cloud.common.core.utils.BeanUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liulei
 * select COLUMN_NAME as name,IS_NULLABLE as requireds,DATA_TYPE as type,ifnull( CHARACTER_MAXIMUM_LENGTH ,NUMERIC_PRECISION) as length,NUMERIC_SCALE as accuracy,COLUMN_COMMENT as comment
 * from information_schema.COLUMNS
 * where table_name = 'user' and table_schema = 'testuser';
 */
@Data
public class MysqlColumn {


    public MysqlColumn(String name, String type, Boolean required, Boolean uni, Integer length, String comment) {
        this.name = name;
        this.type = type;
        this.required = required;
        this.uni = uni;
        this.length = length;
        this.comment = comment;
        this.webComment = comment;
        setEnums();
    }

    private void setEnums() {
        enums = new ArrayList<>();
        if (this.comment.indexOf("->") > 1) {
            String[] str = this.comment.split("->");
            if (str.length == 2) {
                this.webComment = str[0];
                if("主表".equals(str[1])){
                    this.primaryTable = Boolean.TRUE;
                }else {
                    String[] enums = str[1].split("，");
                    for (String e : enums) {
                        String[] es = e.split("：");
                        if (es.length == 2) {
                            this.enums.add(new Enums(es[0].trim(), es[1].trim(), es[1].trim()));
                        }
                    }
                }
            }
        }
    }

    /**
     * 名称
     */
    private String name;

    /**
     * 属性类型
     */
    private String type;

    /**
     * 必填
     */
    private Boolean required;

    /**
     * 是否唯一
     */
    private Boolean uni;

    /**
     * 长度
     */
    private Integer length;

    /**
     * 是否主表
     */
    private Boolean primaryTable;

    /**
     * 功能说明
     */
    private String comment;

    /**
     * web页面字段显示
     */
    private String webComment;

    /**
     * 拆分备注 生成数据
     */
    private List<Enums> enums;

    /**
     * 将tableName转化为className格式  驼峰命名
     *
     * @return
     */
    public String getNameClass() {
        String className = name.toLowerCase();
        return StringUtil.dbToClassName(className);
    }

    public String getPgSqlType() {
        switch (getType()) {
            case "String":
                if (this.getLength() < 200) {
                    return "varchar(" + this.getLength() + ")";
                } else {
                    return "text";
                }
            case "Long":
                return "bigint";
            case "Integer":
                return "int";
            case "java.util.Date":
            case "Date":
                return "timestamp(6)";
            case "Double":
                return "decimal";
            default:
                System.out.println("我没有对应类型:" + type);
                return "";
        }
    }

    public String getPgRequired() {
        if (required) {
            return "null";
        } else {
            return "not null";
        }
    }

    public String getComment() {
        return BeanUtil.replaceBlank(comment);
    }
}
