package com.tianque.scgrid.service.issue.focus.query;
import lombok.Data;

import java.util.Date;

/**
 * @author liulei
 * 事件重点类型关注
 */
@Data
public class IssueFocusTagQuery {

	/**
     * 
     */
    @ApiModelProperty(value = "事件重点类型关注")
    private Long id;

	/**
     * 组织机构
     */
    @ApiModelProperty(value = "事件重点类型关注组织机构")
    private Long orgId;

	/**
     * 组织机构
     */
    @ApiModelProperty(value = "事件重点类型关注组织机构")
    private String orgCode;

	/**
     * 标签ID
     */
    @ApiModelProperty(value = "事件重点类型关注标签ID")
    private Long tagId;

	/**
     * 是否删除
     */
    @ApiModelProperty(value = "事件重点类型关注是否删除")
    private Integer isDelete;
}
