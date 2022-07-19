package com.test.read.bo;

import lombok.Data;

/**
 * @author liulei
 */

@Data
public class DatabaseParam {

    /**
     * 列名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 原始表字段
     */
    private String dbParamName;

    /**
     * 类型
     */
    private String type;

    /**
     * 长度
     */
    private Integer length;

    /**
     * 默认值
     */
    private String def;


    public String getDbParamName() {
        if (this.dbParamName == null || this.dbParamName.length() <= 0) {
            return "未找到对应字段";
        }
        return this.dbParamName;
    }

    public String getMysqlType() {
        if (this.type != null) {
            switch (this.type) {
                case "tinyint":
                    return "tinyint";
                case "boolean":
                    return "smallint";
                case "int2":
                case "integer":
                case "int4":
                case "smallint":
                case "int":
                    return "int";
                case "bigint":
                case "int8":
                    return "bigint";
                case "float":
                case "numeric":
                    return "decimal(" + (this.length + 5) + ",5)";
                case "datetime":
                case "timestamp":
                    return "datetime";
                case "date":
                    return "date";
                case "varchar":
                    return "varchar(" + this.length + ")";
                case "text":
                    return "text";
                case "longtext":
                    return "longtext";
                case "decimal":
                    return "decimal(" + (this.length) + ",0)";


            }
        }
        return "未找到对应字段类型 +" + this.type;
    }
}
