package com.cloud.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * @author liulei
 * 用户信息
 */
@Data
public class UserQuery {

	/**
     * 用户Id
     */
    @ApiModelProperty(value = "用户信息用户Id")
    private Long id;

	/**
     * 用户名
     */
    @ApiModelProperty(value = "用户信息用户名")
    private String userName;

	/**
     * 密码
     */
    @ApiModelProperty(value = "用户信息密码")
    private String password;

    /**
    * 权限
    */
    @ApiModelProperty(value = "用户信息权限")
    private List<Long> permissionsIds;

    /**
    * 标签
    */
    @ApiModelProperty(value = "用户信息标签")
    private List<Long> tagIds;

    /**
    * 部门
    */
    @ApiModelProperty(value = "用户信息部门")
    private List<Long> deptIds;
}
