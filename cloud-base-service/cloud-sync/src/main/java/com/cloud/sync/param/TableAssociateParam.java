package com.cloud.sync.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


/**
 * @author liulei
 */
@Data
@Schema(name = "响应对象", description = "响应对象")
public class TableAssociateParam {

    /**
     * id
     */
    @Schema(description = "id")
    private Long id;

    /**
     * 服务Id
     */
    @Schema(description = "服务Id")
    private Long serveId;


    @Schema(description = "读取配置类型  1:为表->表 ，2:组合表->表")
    private Integer type;

    /**
     * 读取表
     */
    @Schema(description = "读取表")
    private Long readTableId;

    @Schema(description = "读取表")
    private String readerTable;

    /**
     * 写入表
     */
    @Schema(description = "写入表")
    private Long writeTableId;

    @Schema(description = "写入表")
    private String writerTable;

    /**
     * 表字段关联
     */
    private List<AssociateColumnParam> columns;
}