package com.cloud.sync.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.result.ResultBody;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.sync.param.TableMapParam;
import com.cloud.sync.query.TableMapQuery;
import com.cloud.sync.service.TableMapService;
import com.cloud.sync.vo.TableMapVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liulei
 */
@RestController
@RequestMapping("/tableMap")
@Tag(description = "表映射", name = "表映射")
public class TableMapController {

    private TableMapService tableMapService;

    /**
     * 使用构造方法注入
     *
     * @param tableMapService
     */
    public TableMapController(TableMapService tableMapService){
        this.tableMapService= tableMapService;
    }

    /**
     * 保存对象
     *
     * @param tableMapParam
     * @return
     */
    @PostMapping(value = "/save")
    @Operation(summary = "保存", description = "表映射")
    public ResultBody save(@RequestBody TableMapParam tableMapParam) {
        return ResultBody.success(tableMapService.save(tableMapParam));
    }

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/findById")
    @Operation(summary = "通过Id查询数据", description = "表映射")
    public ResultBody findById(Long id) {
        return ResultBody.success(tableMapService.findById(id));
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/findByIds")
    @Operation(summary = "传入多个Id查询数据", description = "表映射")
    public ResultBody findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(tableMapService.findByIds(ids));
    }


    /**
     * 根据查询条件查询列表
     *
     * @param tableMapQuery
     * @return
     */
    @PostMapping(value = "/findByList")
    @Operation(summary = "根据查询条件查询列表", description = "表映射")
    public ResultBody findByList(TableMapQuery tableMapQuery) {
        return ResultBody.success(tableMapService.findByList(tableMapQuery));
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/removeByIds")
    @Operation(summary = "传入多个Id并删除", description = "表映射")
    public ResultBody removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(tableMapService.removeByIds(ids));
    }

    /**
     * 数据分页查询
     *
     * @param tableMapQuery
     * @param pageParam
     * @return
     */
    @PostMapping(value = "/queryPage")
    @Operation(summary = "数据分页查询", description = "表映射")
    public ResultBody queryPage(TableMapQuery tableMapQuery, PageParam pageParam) {
        return ResultBody.success(tableMapService.queryPage(tableMapQuery, pageParam));
    }

}
