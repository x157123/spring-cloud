package com.cloud.excel.test;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.cloud.common.util.db.enums.DataType;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liulei
 */
@Data
public class Column implements Serializable {
    /**
     * 列表
     */
    @ExcelProperty({"名称"})
    @ColumnWidth(25)
    private String columnName;
    /**
     * 数据库原始类型
     */
    @ExcelProperty({"数据类型"})
    @ColumnWidth(15)
    private String dbType;
    /**
     * 数据长度
     */
    @ExcelProperty({"长度"})
    @ColumnWidth(15)
    private Integer length;
    /**
     * 是否必填
     */
    @ExcelProperty({"必填"})
    @ColumnWidth(10)
    private boolean required;
    /**
     * 默认值
     */
    @ExcelProperty({"默认值"})
    @ColumnWidth(10)
    private String defaultVal;
    /**
     * 备注
     */
    @ExcelProperty({"备注"})
    @ColumnWidth(25)
    private String comments;
}
