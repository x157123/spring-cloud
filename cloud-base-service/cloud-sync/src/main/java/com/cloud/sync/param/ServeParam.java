package com.cloud.sync.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * @author liulei
 * 表映射
 */
@Data
@Schema(name = "表映射响应对象", description = "表映射响应对象")
public class ServeParam {

	/**
     * id
     */
    @Schema(description = "id")
    private Long id;

	/**
     * 名称
     */
    @Length(max = 100, message = "表映射名称[ServeVo.name]长度不能超过100个字符")
    @Schema(description = "名称")
    private String name;

	/**
     * 采集数据Id
     */
    @Schema(description = "采集数据Id")
    private Long readConnectId;

	/**
     * 写入数据库Id
     */
    @Schema(description = "写入数据库Id")
    private Long writeConnectId;

	/**
     * 版本
     */
    @Schema(description = "版本")
    private Integer version;
}