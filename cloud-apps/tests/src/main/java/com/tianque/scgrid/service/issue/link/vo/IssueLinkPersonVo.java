package com.tianque.scgrid.service.issue.link.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;

import java.util.Date;

/**
 * @author liulei
 * 事件人员关联
 */
@Data
@ApiModel(value = "事件人员关联响应对象", description = "事件人员关联响应对象")
public class IssueLinkPersonVo {

	/**
     * 
     */
    @ApiModelProperty(value = "事件人员关联")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

	/**
     * 姓名
     */
    @ApiModelProperty(value = "事件人员关联姓名")
    private String name;

	/**
     * 身份证
     */
    @ApiModelProperty(value = "事件人员关联身份证")
    private String idCard;

	/**
     * 电话号码
     */
    @ApiModelProperty(value = "事件人员关联电话号码")
    private String phone;

	/**
     * 备注
     */
    @ApiModelProperty(value = "事件人员关联备注")
    private String remark;

	/**
     * 事件Id
     */
    @ApiModelProperty(value = "事件人员关联事件Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long issueId;

	/**
     * 创建人
     */
    @ApiModelProperty(value = "事件人员关联创建人")
    private String createUser;

	/**
     * 创建日期
     */
    @ApiModelProperty(value = "事件人员关联创建日期")
    private Date createDate;

	/**
     * 修改人
     */
    @ApiModelProperty(value = "事件人员关联修改人")
    private String updateUser;

	/**
     * 修改时间
     */
    @ApiModelProperty(value = "事件人员关联修改时间")
    private Date updateDate;

	/**
     * 是否删除
     */
    @ApiModelProperty(value = "事件人员关联是否删除")
    private Integer isDelete;
}
