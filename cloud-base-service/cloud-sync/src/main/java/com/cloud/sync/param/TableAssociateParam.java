package com.cloud.sync.param;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * @author liulei
 */
@Data
@Schema(name = "响应对象", description = "响应对象")
public class TableAssociateParam {

    public TableAssociateParam(Long serveId, String name, Integer type, Long readTableId, String readerTable, Long writeTableId, String writerTable) {
        this.serveId = serveId;
        this.name = name;
        this.type = type;
        this.readTableId = readTableId;
        this.readerTable = readerTable;
        this.writeTableId = writeTableId;
        this.writerTable = writerTable;
        this.id = IdWorker.getId(this);
    }

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

    @Schema(description = "任务名")
    private String name;

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

}