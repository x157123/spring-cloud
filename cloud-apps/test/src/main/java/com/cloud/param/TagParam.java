package com.cloud.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author liulei
 * 标签
 */
@Data
@ApiModel(value = "标签响应对象", description = "标签响应对象")
public class TagParam {

	/**
     * id
     */
    @ApiModelProperty(value = "id", example = "1")
    private Long id;

	/**
     * 标签名称
     */
    @Length(max = 100, message = "标签标签名称[TagVo.name]长度不能大于100")
    @ApiModelProperty(value = "标签名称")
    private String name;

	/**
     * 描述
     */
    @Length(max = 100, message = "标签描述[TagVo.describe]长度不能大于100")
    @ApiModelProperty(value = "描述")
    private String describe;
}
