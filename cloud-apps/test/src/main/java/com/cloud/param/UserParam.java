package com.cloud.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author liulei
 * 用户信息
 */
@Data
@ApiModel(value = "用户信息响应对象", description = "用户信息响应对象")
public class UserParam {

	/**
     * 用户Id
     */
    @ApiModelProperty(value = "用户Id", example = "1")
    private Long id;

	/**
     * 用户名
     */
    @Length(max = 45, message = "用户信息用户名[UserVo.userName]长度不能大于45")
    @ApiModelProperty(value = "用户名")
    private String userName;

	/**
     * 密码
     */
    @Length(max = 45, message = "用户信息密码[UserVo.password]长度不能大于45")
    @ApiModelProperty(value = "密码")
    private String password;
}
