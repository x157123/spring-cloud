package com.dldorg.bean;

import lombok.Data;

import java.sql.Date;

/**
 * @author liulei
 */
@Data
public class DockingMapping {

    private Long id;
    private Long orgId;
    private String orgCode;
    private String otherOrgName;
    private String orgNo;

    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;
    private int dockingType;
    private int isDeleted;

    public DockingMapping(Long id,Long orgId,String orgCode,String otherOrgName,String orgNo){
        this.id = id;
        this.orgId = orgId;
        this.orgCode = orgCode;
        this.otherOrgName = otherOrgName;
        this.orgNo = orgNo;

        this.createDate = new Date(System.currentTimeMillis());
        this.createUser = "liulei";
        this.updateDate = new Date(System.currentTimeMillis());
        this.dockingType = 2;
        this.isDeleted = 0;
    }

}
