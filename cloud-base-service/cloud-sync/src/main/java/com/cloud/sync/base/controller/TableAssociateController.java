package com.cloud.sync.base.controller;

import com.cloud.common.core.result.ResultBody;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.sync.query.TableAssociateQuery;
import com.cloud.sync.base.service.TableAssociateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liulei
 */
@RestController
@RequestMapping("/tableAssociate")
@Tag(description = "", name = "")
public class TableAssociateController {

    private final TableAssociateService tableAssociateService;

    /**
     * 使用构造方法注入
     *
     * @param tableAssociateService 注册
     */
    public TableAssociateController(TableAssociateService tableAssociateService) {
        this.tableAssociateService = tableAssociateService;
    }

    /**
     * 通过Id查询数据
     *
     * @param id 查询ID
     * @return 返回查询结果
     */
    @GetMapping(value = "/findById")
    @Operation(summary = "通过Id查询数据", description = "")
    public ResultBody findById(Long id) {
        return ResultBody.success(tableAssociateService.findById(id));
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids 查询多个Id
     * @return 返回List结果集
     */
    @PostMapping(value = "/findByIds")
    @Operation(summary = "传入多个Id查询数据", description = "")
    public ResultBody findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(tableAssociateService.findByIds(ids));
    }

    /**
     * 根据查询条件查询列表
     *
     * @param tableAssociateQuery 查询条件
     * @return 返回List结果集
     */
    @PostMapping(value = "/findByList")
    @Operation(summary = "根据查询条件查询列表", description = "")
    public ResultBody findByList(TableAssociateQuery tableAssociateQuery) {
        return ResultBody.success(tableAssociateService.findByList(tableAssociateQuery));
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids 删除多个Id
     * @return 返回是否成功
     */
    @PostMapping(value = "/removeByIds")
    @Operation(summary = "传入多个Id并删除", description = "")
    public ResultBody removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(tableAssociateService.removeByIds(ids));
    }

    /**
     * 数据分页查询
     *
     * @param tableAssociateQuery 分页查询条件
     * @param pageParam           分页参数
     * @return 返回分页结果
     */
    @PostMapping(value = "/queryPage")
    @Operation(summary = "数据分页查询", description = "")
    public ResultBody queryPage(@ParameterObject TableAssociateQuery tableAssociateQuery, @ParameterObject PageParam pageParam) {
        return ResultBody.success(tableAssociateService.queryPage(tableAssociateQuery, pageParam));
    }

}