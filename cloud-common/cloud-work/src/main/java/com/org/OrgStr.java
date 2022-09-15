package com.org;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liulei
 */
@Data
public class OrgStr implements Serializable {

    @ExcelProperty(index = 0)
    private String xian;

    @ExcelProperty(index = 1)
    private String xiang;

    @ExcelProperty(index = 2)
    private String sq;
}
