package com.cloud.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import com.cloud.vo.UserVo;

/**
 * @author liulei
 * 部门
 */
@Data
@ApiModel(value = "部门响应对象", description = "部门响应对象")
public class DeptVo {

	/**
     * id
     */
    @ApiModelProperty(value = "部门id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

	/**
     * 部门名称
     */
    @ApiModelProperty(value = "部门部门名称")
    private String name;

	/**
     * 排序
     */
    @ApiModelProperty(value = "部门排序")
    private Integer seq;

	/**
     * 父节点
     */
    @ApiModelProperty(value = "部门父节点")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long nodeId;

    /**
    * 用户信息
    */
    @ApiModelProperty(value = "部门用户信息")
    private List<UserVo> userVOList;
}
