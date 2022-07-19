package com.user;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liulei
 */
@Data
public class UserOrg implements Serializable {
    @ExcelProperty(value = "账号", index = 0)
    private String userName;

    @ExcelProperty(value = "姓名", index = 1)
    private String name;

    @ExcelProperty(value = "原始orgId", index = 2)
    private String orgId;

    @ExcelProperty(value = "原始orgCode", index = 3)
    private String orgCode;

    @ExcelProperty(value = "新orgId", index = 4)
    private String newOrgId;

    @ExcelProperty(value = "新orgCode", index = 5)
    private String newOrgCode;

    @ExcelProperty(value = "市", index = 6)
    private String cit;

    @ExcelProperty(value = "区县", index = 7)
    private String xian;

    @ExcelProperty(value = "乡镇", index = 8)
    private String xiang;

    @ExcelProperty(value = "村", index = 9)
    private String chun;

    @ExcelProperty(value = "网格", index = 10)
    private String grid;

    @ExcelProperty(value = "sql", index = 11)
    private String sql;

    private String orgLever;

    private String orgType;

    public String getUserName() {
        return userName;
    }

    public String getFullOrgName(){
        return "四川省/"+this.cit+"/"+this.xian+"/"+this.xiang+"/"+this.chun+"/"+this.grid;
    }

}
