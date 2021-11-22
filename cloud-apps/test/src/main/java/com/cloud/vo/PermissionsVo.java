package com.cloud.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import com.cloud.vo.UserVo;
import com.cloud.vo.MenuVo;

/**
 * @author liulei
 * 权限
 */
@Data
@ApiModel(value = "权限响应对象", description = "权限响应对象")
public class PermissionsVo {

	/**
     * id
     */
    @ApiModelProperty(value = "权限id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

	/**
     * 权限名称
     */
    @ApiModelProperty(value = "权限权限名称")
    private String name;

    /**
    * 用户信息
    */
    @ApiModelProperty(value = "权限用户信息")
    private List<UserVo> userVOList;

    /**
    * 菜单权限
    */
    @ApiModelProperty(value = "权限菜单权限")
    private List<MenuVo> menuVOList;
}
