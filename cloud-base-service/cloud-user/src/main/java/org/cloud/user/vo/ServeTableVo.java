package org.cloud.user.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * @author liulei
 * 同步表
 */
@Data
@Schema(name="test",description ="test" )
public class ServeTableVo {

	/**
     * 数据表映射
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(name="tableMapId",description ="用户信息" )
    private Long tableMapId;
}
