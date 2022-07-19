package com.dldorg.org;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author liulei
 */
@Data
public class OrgTest implements Serializable {

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
     * 备注
     */
    private String remark;

    /**
     * 处理后的名字
     */
    private String orgNewName;
}
