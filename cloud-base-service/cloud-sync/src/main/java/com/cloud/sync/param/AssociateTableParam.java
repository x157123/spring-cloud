package com.cloud.sync.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class AssociateTableParam {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "读取table")
    private String readTable;

    @Schema(description = "读取table类型")
    private Integer type;

    @Schema(description = "写入table")
    private String writeTable;

    @Schema(description = "表字段关联")
    private List<AssociateColumnParam> columns;
}