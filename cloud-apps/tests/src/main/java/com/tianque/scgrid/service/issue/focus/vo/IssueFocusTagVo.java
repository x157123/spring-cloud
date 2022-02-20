package com.tianque.scgrid.service.issue.focus.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;

import java.util.Date;

/**
 * @author liulei
 * 事件重点类型关注
 */
@Data
@ApiModel(value = "事件重点类型关注响应对象", description = "事件重点类型关注响应对象")
public class IssueFocusTagVo {

	/**
     * 
     */
    @ApiModelProperty(value = "事件重点类型关注")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

	/**
     * 组织机构
     */
    @ApiModelProperty(value = "事件重点类型关注组织机构")
    @JsonSerialize(using = ToStringSerializer.class)
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
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tagId;

	/**
     * 创建人
     */
    @ApiModelProperty(value = "事件重点类型关注创建人")
    private String createUser;

	/**
     * 创建日期
     */
    @ApiModelProperty(value = "事件重点类型关注创建日期")
    private Date createDate;

	/**
     * 修改人
     */
    @ApiModelProperty(value = "事件重点类型关注修改人")
    private String updateUser;

	/**
     * 修改时间
     */
    @ApiModelProperty(value = "事件重点类型关注修改时间")
    private Date updateDate;

	/**
     * 是否删除
     */
    @ApiModelProperty(value = "事件重点类型关注是否删除")
    private Integer isDelete;
}
