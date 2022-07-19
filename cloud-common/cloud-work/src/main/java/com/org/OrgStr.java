package com.org;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liulei
 */
@Data
public class OrgStr implements Serializable {

    @ExcelProperty(value = {"行政区划","县（市、区）"}, index = 1)
    private String xian;

    @ExcelProperty(value = {"行政区划","乡（镇、街道）"}, index = 2)
    private String xiang;

    @ExcelProperty(value = {"行政区划","村（社区）"}, index = 3)
    private String sq;
}
