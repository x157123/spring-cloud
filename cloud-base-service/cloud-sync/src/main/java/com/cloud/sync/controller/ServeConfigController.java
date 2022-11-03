package com.cloud.sync.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.result.ResultBody;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.sync.param.ServeConfigParam;
import com.cloud.sync.query.ServeConfigQuery;
import com.cloud.sync.service.ServeConfigService;
import com.cloud.sync.vo.ServeConfigVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liulei
 */
@RestController
@RequestMapping("/serveConfig")
@Tag(description = "同步启动服务", name = "同步启动服务")
public class ServeConfigController {

    private ServeConfigService serveConfigService;

    /**
     * 使用构造方法注入
     *
     * @param serveConfigService
     */
    public ServeConfigController(ServeConfigService serveConfigService){
        this.serveConfigService= serveConfigService;
    }

    /**
     * 保存对象
     *
     * @param serveConfigParam
     * @return
     */
    @PostMapping(value = "/save")
    @Operation(summary = "保存", description = "同步启动服务")
    public ResultBody save(@RequestBody ServeConfigParam serveConfigParam) {
        return ResultBody.success(serveConfigService.save(serveConfigParam));
    }

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/findById")
    @Operation(summary = "通过Id查询数据", description = "同步启动服务")
    public ResultBody findById(Long id) {
        return ResultBody.success(serveConfigService.findById(id));
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/findByIds")
    @Operation(summary = "传入多个Id查询数据", description = "同步启动服务")
    public ResultBody findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(serveConfigService.findByIds(ids));
    }


    /**
     * 根据查询条件查询列表
     *
     * @param serveConfigQuery
     * @return
     */
    @PostMapping(value = "/findByList")
    @Operation(summary = "根据查询条件查询列表", description = "同步启动服务")
    public ResultBody findByList(ServeConfigQuery serveConfigQuery) {
        return ResultBody.success(serveConfigService.findByList(serveConfigQuery));
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/removeByIds")
    @Operation(summary = "传入多个Id并删除", description = "同步启动服务")
    public ResultBody removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(serveConfigService.removeByIds(ids));
    }

    /**
     * 数据分页查询
     *
     * @param serveConfigQuery
     * @param pageParam
     * @return
     */
    @PostMapping(value = "/queryPage")
    @Operation(summary = "数据分页查询", description = "同步启动服务")
    public ResultBody queryPage(ServeConfigQuery serveConfigQuery, PageParam pageParam) {
        return ResultBody.success(serveConfigService.queryPage(serveConfigQuery, pageParam));
    }

}
