package com.tianque.scgrid.service.issue.focus.param;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

import java.util.Date;

/**
 * @author liulei
 * 事件重点类型关注
 */
@Data
@ApiModel(value = "事件重点类型关注响应对象", description = "事件重点类型关注响应对象")
public class IssueFocusTagParam {

	/**
     * 
     */
    @ApiModelProperty(value = "", example = "1")
    private Long id;

	/**
     * 组织机构
     */
    @ApiModelProperty(value = "组织机构", example = "1")
    private Long orgId;

	/**
     * 组织机构
     */
    @Length(max = 32, message = "事件重点类型关注组织机构[IssueFocusTagVo.orgCode]长度不能大于32")
    @ApiModelProperty(value = "组织机构")
    private String orgCode;

	/**
     * 标签ID
     */
    @ApiModelProperty(value = "标签ID", example = "1")
    private Long tagId;

	/**
     * 是否删除
     */
    @NotBlank(message = "事件重点类型关注是否删除[IssueFocusTagVo.isDelete]不能为null")
    @ApiModelProperty(value = "是否删除", required = true)
    private Integer isDelete;
}
