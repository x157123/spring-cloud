package com.cloud.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author liulei
 * 照片
 */
@Data
@ApiModel(value = "照片响应对象", description = "照片响应对象")
public class ImgVo {

	/**
     * Id
     */
    @ApiModelProperty(value = "照片Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

	/**
     * 用户ID
     */
    @ApiModelProperty(value = "照片用户ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

	/**
     * 图片路径
     */
    @ApiModelProperty(value = "照片图片路径")
    private String url;
}
