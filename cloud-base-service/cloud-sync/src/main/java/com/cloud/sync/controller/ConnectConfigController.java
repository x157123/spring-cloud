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

    private final ConnectConfigService connectConfigService;

    /**
     * 使用构造方法注入
     *
     * @param connectConfigService    注册数据库配置
     */
    public ConnectConfigController(ConnectConfigService connectConfigService){
        this.connectConfigService= connectConfigService;
    }

    /**
     * 保存对象
     *
     * @param connectConfigParam  数据库配置参数
     * @return  返回是否成功
     */
    @PostMapping(value = "/save")
    @Operation(summary = "保存", description = "数据库配置")
    public ResultBody save(@RequestBody ConnectConfigParam connectConfigParam) {
        return ResultBody.success(connectConfigService.save(connectConfigParam));
    }

    /**
     * 通过Id查询数据
     *
     * @param id   查询ID
     * @return  返回查询结果
     */
    @GetMapping(value = "/findById")
    @Operation(summary = "通过Id查询数据", description = "数据库配置")
    public ResultBody findById(Long id) {
        return ResultBody.success(connectConfigService.findById(id));
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids  查询多个Id
     * @return  返回List结果集
     */
    @PostMapping(value = "/findByIds")
    @Operation(summary = "传入多个Id查询数据", description = "数据库配置")
    public ResultBody findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(connectConfigService.findByIds(ids));
    }


    /**
     * 根据查询条件查询列表
     *
     * @param connectConfigQuery 查询条件
     * @return  返回List结果集
     */
    @PostMapping(value = "/findByList")
    @Operation(summary = "根据查询条件查询列表", description = "数据库配置")
    public ResultBody findByList(ConnectConfigQuery connectConfigQuery) {
        return ResultBody.success(connectConfigService.findByList(connectConfigQuery));
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids  删除多个Id
     * @return  返回是否成功
     */
    @PostMapping(value = "/removeByIds")
    @Operation(summary = "传入多个Id并删除", description = "数据库配置")
    public ResultBody removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(connectConfigService.removeByIds(ids));
    }

    /**
     * 数据分页查询
     *
     * @param connectConfigQuery  分页查询条件
     * @param pageParam    分页参数
     * @return  返回分页结果
     */
    @PostMapping(value = "/queryPage")
    @Operation(summary = "数据分页查询", description = "数据库配置")
    public ResultBody queryPage(ConnectConfigQuery connectConfigQuery, PageParam pageParam) {
        return ResultBody.success(connectConfigService.queryPage(connectConfigQuery, pageParam));
    }

}
