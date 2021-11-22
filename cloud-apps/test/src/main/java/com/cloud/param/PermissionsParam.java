package com.cloud.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author liulei
 * 权限
 */
@Data
@ApiModel(value = "权限响应对象", description = "权限响应对象")
public class PermissionsParam {

	/**
     * id
     */
    @ApiModelProperty(value = "id", example = "1")
    private Long id;

	/**
     * 权限名称
     */
    @Length(max = 100, message = "权限权限名称[PermissionsVo.name]长度不能大于100")
    @ApiModelProperty(value = "权限名称")
    private String name;
}
