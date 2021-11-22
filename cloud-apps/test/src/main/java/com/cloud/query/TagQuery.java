package com.cloud.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * @author liulei
 * 标签
 */
@Data
public class TagQuery {

	/**
     * id
     */
    @ApiModelProperty(value = "标签id")
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
    private List<Long> userIds;
}
