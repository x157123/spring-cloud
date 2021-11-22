package com.cloud.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import com.cloud.vo.MenuVo;
import com.cloud.vo.PermissionsVo;

/**
 * @author liulei
 * 菜单权限
 */
@Data
@ApiModel(value = "菜单权限响应对象", description = "菜单权限响应对象")
public class MenuVo {

	/**
     * id
     */
    @ApiModelProperty(value = "菜单权限id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

	/**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单权限菜单名称")
    private String name;

	/**
     * 排序
     */
    @ApiModelProperty(value = "菜单权限排序")
    private Integer seq;

	/**
     * 父类id
     */
    @ApiModelProperty(value = "菜单权限父类id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long nodeId;

	/**
     * 地址
     */
    @ApiModelProperty(value = "菜单权限地址")
    private String url;

	/**
     * 外部跳转地址
     */
    @ApiModelProperty(value = "菜单权限外部跳转地址")
    private String externalUrl;

	/**
     * 菜单权限
     */
    @ApiModelProperty(value = "菜单权限菜单权限")
    private List<MenuVo> menuVOList;

    /**
    * 权限
    */
    @ApiModelProperty(value = "菜单权限权限")
    private List<PermissionsVo> permissionsVOList;
}
