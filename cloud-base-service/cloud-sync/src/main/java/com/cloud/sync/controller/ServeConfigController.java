package com.cloud.sync.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.result.ResultBody;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.sync.param.serveConfigParam;
import com.cloud.sync.query.serveConfigQuery;
import com.cloud.sync.service.serveConfigService;
import com.cloud.sync.vo.serveConfigVo;
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
public class serveConfigController {

    private final serveConfigService serveConfigService;

    /**
     * 使用构造方法注入
     *
     * @param serveConfigService    注册同步启动服务
     */
    public serveConfigController(serveConfigService serveConfigService){
        this.serveConfigService= serveConfigService;
    }

    /**
     * 保存对象
     *
     * @param serveConfigParam  同步启动服务参数
     * @return  返回是否成功
     */
    @PostMapping(value = "/save")
    @Operation(summary = "保存", description = "同步启动服务")
    public ResultBody save(@RequestBody serveConfigParam serveConfigParam) {
        return ResultBody.success(serveConfigService.save(serveConfigParam));
    }

    /**
     * 通过Id查询数据
     *
     * @param id   查询ID
     * @return  返回查询结果
     */
    @GetMapping(value = "/findById")
    @Operation(summary = "通过Id查询数据", description = "同步启动服务")
    public ResultBody findById(Long id) {
        return ResultBody.success(serveConfigService.findById(id));
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids  查询多个Id
     * @return  返回List结果集
     */
    @PostMapping(value = "/findByIds")
    @Operation(summary = "传入多个Id查询数据", description = "同步启动服务")
    public ResultBody findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(serveConfigService.findByIds(ids));
    }


    /**
     * 根据查询条件查询列表
     *
     * @param serveConfigQuery 查询条件
     * @return  返回List结果集
     */
    @PostMapping(value = "/findByList")
    @Operation(summary = "根据查询条件查询列表", description = "同步启动服务")
    public ResultBody findByList(serveConfigQuery serveConfigQuery) {
        return ResultBody.success(serveConfigService.findByList(serveConfigQuery));
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids  删除多个Id
     * @return  返回是否成功
     */
    @PostMapping(value = "/removeByIds")
    @Operation(summary = "传入多个Id并删除", description = "同步启动服务")
    public ResultBody removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(serveConfigService.removeByIds(ids));
    }

    /**
     * 数据分页查询
     *
     * @param serveConfigQuery  分页查询条件
     * @param pageParam    分页参数
     * @return  返回分页结果
     */
    @PostMapping(value = "/queryPage")
    @Operation(summary = "数据分页查询", description = "同步启动服务")
    public ResultBody queryPage(serveConfigQuery serveConfigQuery, PageParam pageParam) {
        return ResultBody.success(serveConfigService.queryPage(serveConfigQuery, pageParam));
    }

}
