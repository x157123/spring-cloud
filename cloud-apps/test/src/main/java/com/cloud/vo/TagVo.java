package com.cloud.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import com.cloud.vo.UserVo;

/**
 * @author liulei
 * 标签
 */
@Data
@ApiModel(value = "标签响应对象", description = "标签响应对象")
public class TagVo {

	/**
     * id
     */
    @ApiModelProperty(value = "标签id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

	/**
     * 标签名称
     */
    @ApiModelProperty(value = "标签标签名称")
    private String name;

	/**
     * 描述
     */
    @ApiModelProperty(value = "标签描述")
    private String describe;

    /**
    * 用户信息
    */
    @ApiModelProperty(value = "标签用户信息")
    private List<UserVo> userVOList;
}
