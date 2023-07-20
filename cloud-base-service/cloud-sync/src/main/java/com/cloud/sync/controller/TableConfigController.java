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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liulei
 */
@RestController
@RequestMapping("/tableConfig")
@Tag(description = "同步表配置", name = "同步表配置")
public class TableConfigController {

    private final TableConfigService tableConfigService;

    /**
     * 使用构造方法注入
     *
     * @param tableConfigService    注册同步表配置
     */
    public TableConfigController(TableConfigService tableConfigService){
        this.tableConfigService= tableConfigService;
    }

    /**
     * 保存对象
     *
     * @param tableConfigParam  同步表配置参数
     * @return  返回是否成功
     */
    @PostMapping(value = "/save")
    @Operation(summary = "保存", description = "同步表配置")
    public ResultBody save(@RequestBody TableConfigParam tableConfigParam) {
        return ResultBody.success(tableConfigService.save(tableConfigParam));
    }

    /**
     * 通过Id查询数据
     *
     * @param id   查询ID
     * @return  返回查询结果
     */
    @GetMapping(value = "/findById")
    @Operation(summary = "通过Id查询数据", description = "同步表配置")
    public ResultBody findById(Long id) {
        return ResultBody.success(tableConfigService.findById(id));
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids  查询多个Id
     * @return  返回List结果集
     */
    @PostMapping(value = "/findByIds")
    @Operation(summary = "传入多个Id查询数据", description = "同步表配置")
    public ResultBody findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(tableConfigService.findByIds(ids));
    }

    /**
     * 根据查询条件查询列表
     *
     * @param tableConfigQuery 查询条件
     * @return  返回List结果集
     */
    @PostMapping(value = "/findByList")
    @Operation(summary = "根据查询条件查询列表", description = "同步表配置")
    public ResultBody findByList(TableConfigQuery tableConfigQuery) {
        return ResultBody.success(tableConfigService.findByList(tableConfigQuery));
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids  删除多个Id
     * @return  返回是否成功
     */
    @PostMapping(value = "/removeByIds")
    @Operation(summary = "传入多个Id并删除", description = "同步表配置")
    public ResultBody removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(tableConfigService.removeByIds(ids));
    }

    /**
     * 数据分页查询
     *
     * @param tableConfigQuery  分页查询条件
     * @param pageParam    分页参数
     * @return  返回分页结果
     */
    @PostMapping(value = "/queryPage")
    @Operation(summary = "数据分页查询", description = "同步表配置")
    public ResultBody queryPage(@ParameterObject TableConfigQuery tableConfigQuery, @ParameterObject PageParam pageParam) {
        return ResultBody.success(tableConfigService.queryPage(tableConfigQuery, pageParam));
    }

}