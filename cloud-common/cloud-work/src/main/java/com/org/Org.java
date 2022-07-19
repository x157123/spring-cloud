package com.org;

import lombok.Data;

import java.io.Serializable;
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
     * 下辖组织
     */
    private List<Org> orgs;
}
