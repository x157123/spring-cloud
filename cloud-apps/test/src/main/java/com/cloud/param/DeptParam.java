package com.cloud.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author liulei
 * 部门
 */
@Data
@ApiModel(value = "部门响应对象", description = "部门响应对象")
public class DeptParam {

	/**
     * id
     */
    @ApiModelProperty(value = "id", example = "1")
    private Long id;

	/**
     * 部门名称
     */
    @Length(max = 100, message = "部门部门名称[DeptVo.name]长度不能大于100")
    @ApiModelProperty(value = "部门名称")
    private String name;

	/**
     * 排序
     */
    @ApiModelProperty(value = "排序", example = "1")
    private Integer seq;

	/**
     * 父节点
     */
    @ApiModelProperty(value = "父节点", example = "1")
    private Long nodeId;
}
