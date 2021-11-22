package com.cloud.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liulei
 * 用户详情
 */
@Data
public class UserDetailQuery {

	/**
     * Id
     */
    @ApiModelProperty(value = "用户详情Id")
    private Long id;

	/**
     * 用户Id
     */
    @ApiModelProperty(value = "用户详情用户Id")
    private Long userId;

	/**
     * 用户名字
     */
    @ApiModelProperty(value = "用户详情用户名字")
    private String name;

	/**
     * 年龄
     */
    @ApiModelProperty(value = "用户详情年龄")
    private Integer age;
}
