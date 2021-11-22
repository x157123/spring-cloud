package com.cloud.common.mybatis.page;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liulei
 * 分页条件
 */
@Data
public class PageParam implements Serializable {

    public static final int MAX_VALUE = 200;

    private Integer page = 1;

    private Integer rows = 15;

    private String sidx;

    private String sord;

    public PageParam() {
    }

    public PageParam(Integer page, Integer rows) {
        this.page = page;
        this.rows = rows;
    }

    public void setPage(Integer page) {
        if (page != null) {
            this.page = page;
        }
    }

    public void setRows(Integer rows) {
        if (rows != null) {
            this.rows = rows;
            this.rows = rows >= MAX_VALUE ? MAX_VALUE : rows;
        }
    }
}
