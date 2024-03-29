package com.cloud.auto.code.vo;

import com.cloud.auto.code.util.StringUtil;
import com.cloud.common.core.utils.BeanUtil;
import lombok.Data;

/**
 * @author liulei
 * select COLUMN_NAME as name,IS_NULLABLE as requireds,DATA_TYPE as type,ifnull( CHARACTER_MAXIMUM_LENGTH ,NUMERIC_PRECISION) as length,NUMERIC_SCALE as accuracy,COLUMN_COMMENT as comment
 * from information_schema.COLUMNS
 * where table_name = 'user' and table_schema = 'testuser';
 */
@Data
public class Column {

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
    private String required;

    /**
     * 长度
     */
    private Long length;

    /**
     * 精度
     */
    private Long accuracy;
    /**
     * 功能说明
     */
    private String comment;

    /**
     * 字段约束情况 PRI主键  UNI唯一约束
     */
    private String constr;

    /**
     * 获取最少值
     *
     * @return
     */
    public Long getMin() {
        return 0L;
    }

    /**
     * 获取最大值
     *
     * @return
     */
    public Long getMax() {
        return 0L;
    }

    /**
     * 是否是主键
     *
     * @return
     */
    public boolean isPri() {
        if ("PRI".equals(constr)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 是否唯一约束
     *
     * @return
     */
    public boolean isUni() {
        if ("UNI".equals(constr)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 将tableName转化为className格式  驼峰命名
     *
     * @return
     */
    public String getNameClass() {
        String className = name.toLowerCase();
        return StringUtil.dbToClassName(className);
    }


    /**
     * 获取类型
     *
     * @return
     */
    public String getRequiredType() {
        switch (required) {
            //mysql数据中为 非空字段 IS_NULLABLE 所以为相反
            case "YES":
                //当为yes 表示不必填
                return "false";
            default:
                return "true";
        }
    }

    /**
     * 获取类型
     *
     * @return
     */
    public String getJavaType() {
        switch (type) {
            case "VARCHAR2":
            case "CLOB":
            case "BLOB":
            case "blob":
            case "varchar":
            case "text":
            case "longtext":
            case "longblob":
                return "String";
            case "bigint":
            case "NUMBER":
                return "Long";
            case "tinyint":
            case "int":
            case "smallint":
                return "Integer";
            case "date":
            case "DATE":
            case "datetime":
            case "timestamp":
                return "Date";
            case "decimal":
                return "Double";
            default:
                System.out.println("我没有对应类型:" + type);
                return "";
        }
    }

    public String getPgSqlType(){
        switch (getJavaType()) {
            case "String":
                if (this.getLength() < 200){
                    return "varchar(" +this.getLength() + ")";
                }else{
                    return "text";
                }
            case "Long":
                return "bigint";
            case "Integer":
                return "int";
            case "Date":
                return "timestamp(6)";
            case "Double":
                return "decimal";
            default:
                System.out.println("我没有对应类型:" + type);
                return "";
        }
    }

    public String getPgRequired(){
        switch (required) {
            //mysql数据中为 非空字段 IS_NULLABLE 所以为相反
            case "YES":
                //当为yes 表示不必填
                return "null";
            default:
                return "not null";
        }
    }

    public String getComment() {
        return BeanUtil.replaceBlank(comment);
    }
}
