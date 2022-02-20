package com.tianque.scgrid.service.issue.link.param;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

import java.util.Date;

/**
 * @author liulei
 * 事件人员关联
 */
@Data
@ApiModel(value = "事件人员关联响应对象", description = "事件人员关联响应对象")
public class IssueLinkPersonParam {

	/**
     * 
     */
    @ApiModelProperty(value = "", example = "1")
    private Long id;

	/**
     * 姓名
     */
    @Length(max = 32, message = "事件人员关联姓名[IssueLinkPersonVo.name]长度不能大于32")
    @ApiModelProperty(value = "姓名")
    private String name;

	/**
     * 身份证
     */
    @Length(max = 32, message = "事件人员关联身份证[IssueLinkPersonVo.idCard]长度不能大于32")
    @ApiModelProperty(value = "身份证")
    private String idCard;

	/**
     * 电话号码
     */
    @Length(max = 32, message = "事件人员关联电话号码[IssueLinkPersonVo.phone]长度不能大于32")
    @ApiModelProperty(value = "电话号码")
    private String phone;

	/**
     * 备注
     */
    @Length(max = 150, message = "事件人员关联备注[IssueLinkPersonVo.remark]长度不能大于150")
    @ApiModelProperty(value = "备注")
    private String remark;

	/**
     * 事件Id
     */
    @NotBlank(message = "事件人员关联事件Id[IssueLinkPersonVo.issueId]不能为null")
    @ApiModelProperty(value = "事件Id", required = true, example = "1")
    private Long issueId;

	/**
     * 是否删除
     */
    @NotBlank(message = "事件人员关联是否删除[IssueLinkPersonVo.isDelete]不能为null")
    @ApiModelProperty(value = "是否删除", required = true)
    private Integer isDelete;
}
