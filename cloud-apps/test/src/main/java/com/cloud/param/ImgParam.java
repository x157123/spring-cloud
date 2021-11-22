package com.cloud.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author liulei
 * 照片
 */
@Data
@ApiModel(value = "照片响应对象", description = "照片响应对象")
public class ImgParam {

	/**
     * Id
     */
    @ApiModelProperty(value = "Id", example = "1")
    private Long id;

	/**
     * 用户ID
     */
    @NotBlank(message = "照片用户ID[ImgVo.userId]不能为null")
    @ApiModelProperty(value = "用户ID", required = true, example = "1")
    private Long userId;

	/**
     * 图片路径
     */
    @Length(max = 45, message = "照片图片路径[ImgVo.url]长度不能大于45")
    @ApiModelProperty(value = "图片路径")
    private String url;
}
