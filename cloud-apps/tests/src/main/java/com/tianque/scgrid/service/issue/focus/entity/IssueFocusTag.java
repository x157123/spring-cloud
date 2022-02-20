package com.tianque.scgrid.service.issue.focus.entity;

import lombok.Data;
import java.util.Date;

/**
 * @author liulei
 * 事件重点类型关注
 */
@Data
public class IssueFocusTag {

	/**
     * 
     */
    private Long id;

	/**
     * 组织机构
     */
    private Long orgId;

	/**
     * 组织机构
     */
    private String orgCode;

	/**
     * 标签ID
     */
    private Long tagId;

	/**
     * 创建人
     */
    private String createUser;

	/**
     * 创建日期
     */
    private Date createDate;

	/**
     * 修改人
     */
    private String updateUser;

	/**
     * 修改时间
     */
    private Date updateDate;

	/**
     * 是否删除
     */
    private Integer isDelete;
}
