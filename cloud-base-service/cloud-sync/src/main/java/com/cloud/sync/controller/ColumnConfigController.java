package com.cloud.sync.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.result.ResultBody;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.sync.param.ColumnConfigParam;
import com.cloud.sync.query.ColumnConfigQuery;
import com.cloud.sync.service.ColumnConfigService;
import com.cloud.sync.vo.ColumnConfigVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liulei
 */
@RestController
@RequestMapping("/columnConfig")
@Tag(description = "同步数据库列配置", name = "同步数据库列配置")
public class ColumnConfigController {

    private final ColumnConfigService columnConfigService;

    /**
     * 使用构造方法注入
     *
     * @param columnConfigService    注册同步数据库列配置
     */
    public ColumnConfigController(ColumnConfigService columnConfigService){
        this.columnConfigService= columnConfigService;
    }

    /**
     * 保存对象
     *
     * @param columnConfigParam  同步数据库列配置参数
     * @return  返回是否成功
     */
    @PostMapping(value = "/save")
    @Operation(summary = "保存", description = "同步数据库列配置")
    public ResultBody save(@RequestBody ColumnConfigParam columnConfigParam) {
        return ResultBody.success(columnConfigService.save(columnConfigParam));
    }

    /**
     * 通过Id查询数据
     *
     * @param id   查询ID
     * @return  返回查询结果
     */
    @GetMapping(value = "/findById")
    @Operation(summary = "通过Id查询数据", description = "同步数据库列配置")
    public ResultBody findById(Long id) {
        return ResultBody.success(columnConfigService.findById(id));
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids  查询多个Id
     * @return  返回List结果集
     */
    @PostMapping(value = "/findByIds")
    @Operation(summary = "传入多个Id查询数据", description = "同步数据库列配置")
    public ResultBody findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(columnConfigService.findByIds(ids));
    }

    /**
     * 根据查询条件查询列表
     *
     * @param columnConfigQuery 查询条件
     * @return  返回List结果集
     */
    @PostMapping(value = "/findByList")
    @Operation(summary = "根据查询条件查询列表", description = "同步数据库列配置")
    public ResultBody findByList(ColumnConfigQuery columnConfigQuery) {
        return ResultBody.success(columnConfigService.findByList(columnConfigQuery));
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids  删除多个Id
     * @return  返回是否成功
     */
    @PostMapping(value = "/removeByIds")
    @Operation(summary = "传入多个Id并删除", description = "同步数据库列配置")
    public ResultBody removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(columnConfigService.removeByIds(ids));
    }

    /**
     * 数据分页查询
     *
     * @param columnConfigQuery  分页查询条件
     * @param pageParam    分页参数
     * @return  返回分页结果
     */
    @PostMapping(value = "/queryPage")
    @Operation(summary = "数据分页查询", description = "同步数据库列配置")
    public ResultBody queryPage(@ParameterObject ColumnConfigQuery columnConfigQuery, @ParameterObject PageParam pageParam) {
        return ResultBody.success(columnConfigService.queryPage(columnConfigQuery, pageParam));
    }

}