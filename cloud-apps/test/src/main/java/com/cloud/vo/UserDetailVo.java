package com.cloud.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author liulei
 * 用户详情
 */
@Data
@ApiModel(value = "用户详情响应对象", description = "用户详情响应对象")
public class UserDetailVo {

	/**
     * Id
     */
    @ApiModelProperty(value = "用户详情Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

	/**
     * 用户Id
     */
    @ApiModelProperty(value = "用户详情用户Id")
    @JsonSerialize(using = ToStringSerializer.class)
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
