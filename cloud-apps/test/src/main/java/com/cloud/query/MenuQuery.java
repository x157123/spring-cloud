package com.cloud.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * @author liulei
 * 菜单权限
 */
@Data
public class MenuQuery {

	/**
     * id
     */
    @ApiModelProperty(value = "菜单权限id")
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
    * 权限
    */
    @ApiModelProperty(value = "菜单权限权限")
    private List<Long> permissionsIds;
}
