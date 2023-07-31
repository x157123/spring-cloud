package com.cloud.sync.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


/**
 * @author liulei
 * 
 */
@Data
@Schema(name = "响应对象", description = "响应对象")
public class JoinTableParam {

	/**
     * id
     */
    @Schema(description = "id")
    private Long id;

	/**
     * 连接ID
     */
    @Schema(description = "连接ID")
    private Long connectId;

	/**
     * 名称
     */
    @Length(max = 100, message = "名称[JoinTableVo.name]长度不能超过100个字符")
    @Schema(description = "名称")
    private String name;

	/**
     * 关联表
     */
    @Length(max = 200, message = "关联表[JoinTableVo.joinTable]长度不能超过200个字符")
    @Schema(description = "关联表")
    private String joinTable;

	/**
     * 版本
     */
    @Schema(description = "版本")
    private Integer version;
}