package com.dldorg.org;

import lombok.Data;

/**
 * @author liulei
 */

@Data
public class OrgCompare {
    /**
     * 市
     */
    private String city;
    /**
     * 县
     */
    private OrgAll county;
    /**
     * 乡镇
     */
    private OrgAll town;
    /**
     * 村
     */
    private OrgAll village;
    /**
     * 村
     */
    private OrgAll grid;
}
