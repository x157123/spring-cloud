package com.cloud.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liulei
 * 照片
 */
@Data
public class ImgQuery {

	/**
     * Id
     */
    @ApiModelProperty(value = "照片Id")
    private Long id;

	/**
     * 用户ID
     */
    @ApiModelProperty(value = "照片用户ID")
    private Long userId;

	/**
     * 图片路径
     */
    @ApiModelProperty(value = "照片图片路径")
    private String url;
}
