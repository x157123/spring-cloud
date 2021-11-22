package com.cloud.common.util.db.bo;

import com.cloud.common.util.db.enums.DataType;
import lombok.Data;

/**
 * @author liulei
 */
@Data
public class ColumnEntity {
    /**
     * 列表
     */
    private String columnName;
    /**
     * 数据类型
     */
    private String type;

    public DataType getType() {
        switch (this.type) {
            case "boolean":
                return DataType.BOOLEAN;
            case "int2":
                this.setLength(2);
                return DataType.INTEGER;
            case "integer":
            case "int4":
            case "smallint":
                this.setLength(4);
                return DataType.INTEGER;
            case "bigint":
            case "int8":
                this.setLength(8);
                return DataType.LONG;
            case "float":
                return DataType.FLOAT;
            case "numeric":
                if(this.scale<=0){
                    if(this.length>=12) {
                        return DataType.LONG;
                    }else{
                        return DataType.INTEGER;
                    }
                }
                return DataType.DOUBLE;
            case "timestamp without time zone":
            case "timestamp":
                return DataType.DATE;
            case "varchar":
                return DataType.STRING;
            case "text":
                this.setLength(-1);
                return DataType.STRING;
        }
        System.out.println("我是未匹配类型：" + this.type);
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
