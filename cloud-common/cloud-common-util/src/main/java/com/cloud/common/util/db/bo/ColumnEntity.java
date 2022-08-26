package com.cloud.common.util.db.bo;

import com.cloud.common.util.db.enums.DataType;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liulei
 */
@Data
public class ColumnEntity implements Serializable {
    /**
     * 列表
     */
    private String columnName;
    /**
     * 数据类型
     */
    private String type;

    /**
     * 数据库原始类型
     */
    private String dbType;

    public String getDbType() {
        return this.type;
    }

    public static String sqlType2JavaType(String sqlType) {
        switch (sqlType.toLowerCase()){
            case "bit": return "boolean";
            case "tinyint": return "byte";
            case "smallint": return "short";
            case "int": return "int";
            case "bigint": return "long";
            case "float": return "float";
            case "decimal":
            case "numeric":
            case "real":
            case "money":
            case "smallmoney": return "double";
            case "varchar":
            case "char":
            case "nvarchar":
            case "nchar":
            case "text": return "String";
            case "datetime":
            case "date": return "Date";
            case "image": return "Blob";
            case "timestamp": return "Timestamp";
            default: return "String";
        }
    }


    /**
     * 数据类型转化JAVA
     * @param sqlType：类型名称
     * @return
     */
    public static String toSqlToJava(String sqlType) {
        if( sqlType == null || sqlType.trim().length() == 0 ) {
            return sqlType;
        }
        sqlType = sqlType.toLowerCase();
        switch(sqlType){
            case "nvarchar":return "String";
            case "char":return "String";
            case "varchar":return "String";
            case "text":return "String";
            case "nchar":return "String";
            case "blob":return "byte[]";
            case "integer":return "Long";
            case "tinyint":return "Integer";
            case "smallint":return "Integer";
            case "mediumint":return "Integer";
            case "bit":return "Boolean";
            case "bigint":return "java.math.BigInteger";
            case "float":return "Fload";
            case "double":return "Double";
            case "decimal":return "java.math.BigDecimal";
            case "boolean":return "Boolean";
            case "id":return "Long";
            case "date":return "java.util.Date";
            case "datetime":return "java.util.Date";
            case "year":return "java.util.Date";
            case "time":return "java.sql.Time";
            case "timestamp":return "java.sql.Timestamp";
            case "numeric":return "java.math.BigDecimal";
            case "real":return "java.math.BigDecimal";
            case "money":return "Double";
            case "smallmoney":return "Double";
            case "image":return "byte[]";
            default:
                System.out.println("-----------------》转化失败：未发现的类型"+sqlType);
                break;
        }
        return sqlType;
    }

    public DataType getType() {
        if (this.type != null) {
            switch (this.type) {
                case "boolean":
                    return DataType.BOOLEAN;
                case "int2":
                    this.setLength(2);
                    return DataType.INTEGER;
                case "integer":
                case "int4":
                case "int":
                case "smallint":
                    this.setLength(4);
                    return DataType.INTEGER;
                case "bigint":
                case "int8":
                    this.setLength(8);
                    return DataType.LONG;
                case "float":
                    return DataType.FLOAT;
                case "decimal":
                case "numeric":
                    if (this.scale <= 0) {
                        if (this.length >= 12) {
                            return DataType.LONG;
                        } else {
                            return DataType.INTEGER;
                        }
                    }
                    return DataType.DOUBLE;
                case "timestamp without time zone":
                case "timestamp":
                case "date":
                case "datetime":
                    return DataType.DATE;
                case "varchar":
                    return DataType.STRING;
                case "text":
                case "longtext":
                    this.setLength(-1);
                    return DataType.STRING;
            }
        }
        System.out.println("我是未匹配字段及类型：" + this.getColumnName() + ":" + this.type);
        return DataType.STRING;
    }

    /**
     * 数据长度
     */
    private Integer length;
    /**
     * 精度
     */
    private Integer scale;
    /**
     * 备注
     */
    private String comments;
    /**
     * 是否必填
     */
    private boolean required;
    /**
     * 默认值
     */
    private String defaultVal;
    /**
     * 属性类型
     */
    private String attrType;
    /**
     * 其他信息
     */
    private String extra;
}
