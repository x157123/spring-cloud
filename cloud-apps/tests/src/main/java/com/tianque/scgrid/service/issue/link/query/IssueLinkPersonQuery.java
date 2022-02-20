package com.tianque.scgrid.service.issue.link.query;
import lombok.Data;

import java.util.Date;

/**
 * @author liulei
 * 事件人员关联
 */
@Data
public class IssueLinkPersonQuery {

	/**
     * 
     */
    @ApiModelProperty(value = "事件人员关联")
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
    private Long issueId;

	/**
     * 是否删除
     */
    @ApiModelProperty(value = "事件人员关联是否删除")
    private Integer isDelete;
}
