package com.cloud.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author liulei
 * 菜单权限
 */
@Data
@ApiModel(value = "菜单权限响应对象", description = "菜单权限响应对象")
public class MenuParam {

	/**
     * id
     */
    @ApiModelProperty(value = "id", example = "1")
    private Long id;

	/**
     * 菜单名称
     */
    @Length(max = 100, message = "菜单权限菜单名称[MenuVo.name]长度不能大于100")
    @ApiModelProperty(value = "菜单名称")
    private String name;

	/**
     * 排序
     */
    @ApiModelProperty(value = "排序", example = "1")
    private Integer seq;

	/**
     * 父类id
     */
    @ApiModelProperty(value = "父类id", example = "1")
    private Long nodeId;

	/**
     * 地址
     */
    @Length(max = 100, message = "菜单权限地址[MenuVo.url]长度不能大于100")
    @ApiModelProperty(value = "地址")
    private String url;

	/**
     * 外部跳转地址
     */
    @Length(max = 100, message = "菜单权限外部跳转地址[MenuVo.externalUrl]长度不能大于100")
    @ApiModelProperty(value = "外部跳转地址")
    private String externalUrl;
}
