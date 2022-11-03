package com.cloud.sync.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.result.ResultBody;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.sync.param.TableConfigParam;
import com.cloud.sync.query.TableConfigQuery;
import com.cloud.sync.service.TableConfigService;
import com.cloud.sync.vo.TableConfigVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liulei
 */
@RestController
@RequestMapping("/tableConfig")
@Tag(description = "", name = "")
public class TableConfigController {

    private TableConfigService tableConfigService;

    /**
     * 使用构造方法注入
     *
     * @param tableConfigService
     */
    public TableConfigController(TableConfigService tableConfigService){
        this.tableConfigService= tableConfigService;
    }

    /**
     * 保存对象
     *
     * @param tableConfigParam
     * @return
     */
    @PostMapping(value = "/save")
    @Operation(summary = "保存", description = "")
    public ResultBody save(@RequestBody TableConfigParam tableConfigParam) {
        return ResultBody.success(tableConfigService.save(tableConfigParam));
    }

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/findById")
    @Operation(summary = "通过Id查询数据", description = "")
    public ResultBody findById(Long id) {
        return ResultBody.success(tableConfigService.findById(id));
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/findByIds")
    @Operation(summary = "传入多个Id查询数据", description = "")
    public ResultBody findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(tableConfigService.findByIds(ids));
    }


    /**
     * 根据查询条件查询列表
     *
     * @param tableConfigQuery
     * @return
     */
    @PostMapping(value = "/findByList")
    @Operation(summary = "根据查询条件查询列表", description = "")
    public ResultBody findByList(TableConfigQuery tableConfigQuery) {
        return ResultBody.success(tableConfigService.findByList(tableConfigQuery));
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/removeByIds")
    @Operation(summary = "传入多个Id并删除", description = "")
    public ResultBody removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(tableConfigService.removeByIds(ids));
    }

    /**
     * 数据分页查询
     *
     * @param tableConfigQuery
     * @param pageParam
     * @return
     */
    @PostMapping(value = "/queryPage")
    @Operation(summary = "数据分页查询", description = "")
    public ResultBody queryPage(TableConfigQuery tableConfigQuery, PageParam pageParam) {
        return ResultBody.success(tableConfigService.queryPage(tableConfigQuery, pageParam));
    }

}
