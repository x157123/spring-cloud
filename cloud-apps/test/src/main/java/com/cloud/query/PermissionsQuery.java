package com.cloud.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * @author liulei
 * 权限
 */
@Data
public class PermissionsQuery {

	/**
     * id
     */
    @ApiModelProperty(value = "权限id")
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
    private List<Long> userIds;

    /**
    * 菜单权限
    */
    @ApiModelProperty(value = "权限菜单权限")
    private List<Long> menuIds;
}
