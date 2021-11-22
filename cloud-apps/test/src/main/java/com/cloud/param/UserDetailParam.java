package com.cloud.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author liulei
 * 用户详情
 */
@Data
@ApiModel(value = "用户详情响应对象", description = "用户详情响应对象")
public class UserDetailParam {

	/**
     * Id
     */
    @ApiModelProperty(value = "Id", example = "1")
    private Long id;

	/**
     * 用户Id
     */
    @NotBlank(message = "用户详情用户Id[UserDetailVo.userId]不能为null")
    @ApiModelProperty(value = "用户Id", required = true, example = "1")
    private Long userId;

	/**
     * 用户名字
     */
    @Length(max = 45, message = "用户详情用户名字[UserDetailVo.name]长度不能大于45")
    @ApiModelProperty(value = "用户名字")
    private String name;

	/**
     * 年龄
     */
    @ApiModelProperty(value = "年龄", example = "1")
    private Integer age;
}
