package com.org;

import lombok.Data;

import java.util.List;

/**
 * @author liulei
 */
@Data
public class OrgCompareAll {

    /**
     * 市
     */
    private String city;

    /**
     * 下辖数据
     */
    List<OrgCompare> orgCompare;
}
