package com.cloud.sync.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AssociateColumnParam {

    /**
     * 读取字段
     */
    @Schema(description = "读取字段")
    private String readColumn;

    /**
     * 写入字段
     */
    @Schema(description = "写入字段")
    private String writeColumn;

    /**
     * 写入表是否是主键
     */
    @Schema(description = "写入表是否是主键")
    private Integer key;

    /**
     * 数据加工
     */
    @Schema(description = "数据加工")
    private String convertFun;

    /**
     * 默认值
     */
    @Schema(description = "默认值")
    private String defaultValue;

}