package com.dldorg.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liulei
 */
@Data
public class User implements Serializable {

    private String orgFullName;

    /**
     * 大联动用户ID
     */
    private String dId;

    private String dUserName;

    private String dName;


    /**
     * 社管用户信息
     */
    private String sId;

    private String sUserName;

    private String sName;

    private Integer size;

    public void addSize() {
        if (size == null) {
            size = 1;
        } else {
            size += 1;
        }
    }
}
