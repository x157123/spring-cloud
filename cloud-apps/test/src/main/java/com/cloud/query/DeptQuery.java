package com.cloud.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * @author liulei
 * 部门
 */
@Data
public class DeptQuery {

	/**
     * id
     */
    @ApiModelProperty(value = "部门id")
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
    private Long nodeId;

    /**
    * 用户信息
    */
    @ApiModelProperty(value = "部门用户信息")
    private List<Long> userIds;
}
