package com.tianque.scgrid.service.issue.link.entity;

import lombok.Data;
import java.util.Date;

/**
 * @author liulei
 * 事件人员关联
 */
@Data
public class IssueLinkPerson {

	/**
     * 
     */
    private Long id;

	/**
     * 姓名
     */
    private String name;

	/**
     * 身份证
     */
    private String idCard;

	/**
     * 电话号码
     */
    private String phone;

	/**
     * 备注
     */
    private String remark;

	/**
     * 事件Id
     */
    private Long issueId;

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
