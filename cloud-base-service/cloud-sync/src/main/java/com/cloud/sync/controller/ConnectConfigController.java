package com.cloud.sync.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.result.ResultBody;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.sync.param.ConnectConfigParam;
import com.cloud.sync.query.ConnectConfigQuery;
import com.cloud.sync.service.ConnectConfigService;
import com.cloud.sync.vo.ConnectConfigVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liulei
 */
@RestController
@RequestMapping("/connectConfig")
@Tag(description = "数据库配置", name = "数据库配置")
public class ConnectConfigController {

    private ConnectConfigService connectConfigService;

    /**
     * 使用构造方法注入
     *
     * @param connectConfigService
     */
    public ConnectConfigController(ConnectConfigService connectConfigService){
        this.connectConfigService= connectConfigService;
    }

    /**
     * 保存对象
     *
     * @param connectConfigParam
     * @return
     */
    @PostMapping(value = "/save")
    @Operation(summary = "保存", description = "数据库配置")
    public ResultBody save(@RequestBody ConnectConfigParam connectConfigParam) {
        return ResultBody.success(connectConfigService.save(connectConfigParam));
    }

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/findById")
    @Operation(summary = "通过Id查询数据", description = "数据库配置")
    public ResultBody findById(Long id) {
        return ResultBody.success(connectConfigService.findById(id));
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/findByIds")
    @Operation(summary = "传入多个Id查询数据", description = "数据库配置")
    public ResultBody findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(connectConfigService.findByIds(ids));
    }


    /**
     * 根据查询条件查询列表
     *
     * @param connectConfigQuery
     * @return
     */
    @PostMapping(value = "/findByList")
    @Operation(summary = "根据查询条件查询列表", description = "数据库配置")
    public ResultBody findByList(ConnectConfigQuery connectConfigQuery) {
        return ResultBody.success(connectConfigService.findByList(connectConfigQuery));
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/removeByIds")
    @Operation(summary = "传入多个Id并删除", description = "数据库配置")
    public ResultBody removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(connectConfigService.removeByIds(ids));
    }

    /**
     * 数据分页查询
     *
     * @param connectConfigQuery
     * @param pageParam
     * @return
     */
    @PostMapping(value = "/queryPage")
    @Operation(summary = "数据分页查询", description = "数据库配置")
    public ResultBody queryPage(ConnectConfigQuery connectConfigQuery, PageParam pageParam) {
        return ResultBody.success(connectConfigService.queryPage(connectConfigQuery, pageParam));
    }

}
