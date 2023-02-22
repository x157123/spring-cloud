package com.shuqiu;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liulei
 */
@Data
public class Orgs implements Serializable {

    /**
     * orgId
     */
    private Long orgId;

    /**
     * 上级
     */
    private String parentId;


    private String orgCode;
    /**
     * orgName
     */
    private String orgName;



    private String orgFullName;

    private String seq;
}
