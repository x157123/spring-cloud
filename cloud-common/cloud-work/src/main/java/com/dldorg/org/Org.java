package com.dldorg.org;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liulei
 */
@Data
public class Org implements Serializable {

    /**
     * orgId
     */
    private Long id;

    /**
     * 上级
     */
    private Long parentId;

    /**
     * orgName
     */
    private String orgName;

    /**
     * 排序
     */
    private Long seq;

    private boolean sg;

    private boolean exist = false;

    /**
     * 最大部门序号
     */
    private Long deptMax;

    /**
     * 下辖组织
     */
    private List<Org> orgs;


    private String orgType;


    private String orgCode;


    private String orgFullName;

    /**
     * 相同OrgName
     */
    private List<Long> sameOrg;

    /**
     * 用户关联
     */
    private String orgInternalCode;


    public void addSameOrgId(Long orgId){
        if(sameOrg == null){
            sameOrg = new ArrayList<>();
        }
        sameOrg.add(orgId);
    }
}
