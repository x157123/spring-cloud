package com.cloud.sync.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.result.ResultBody;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.sync.param.JoinTableParam;
import com.cloud.sync.query.JoinTableQuery;
import com.cloud.sync.service.JoinTableService;
import com.cloud.sync.vo.JoinTableVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liulei
 */
@RestController
@RequestMapping("/joinTable")
@Tag(description = "", name = "")
public class JoinTableController {

    private final JoinTableService joinTableService;

    /**
     * 使用构造方法注入
     *
     * @param joinTableService    注册
     */
    public JoinTableController(JoinTableService joinTableService){
        this.joinTableService= joinTableService;
    }

    /**
     * 保存对象
     *
     * @param joinTableParam  参数
     * @return  返回是否成功
     */
    @PostMapping(value = "/save")
    @Operation(summary = "保存", description = "")
    public ResultBody save(@RequestBody JoinTableParam joinTableParam) {
        return ResultBody.success(joinTableService.save(joinTableParam));
    }

    /**
     * 通过Id查询数据
     *
     * @param id   查询ID
     * @return  返回查询结果
     */
    @GetMapping(value = "/findById")
    @Operation(summary = "通过Id查询数据", description = "")
    public ResultBody findById(Long id) {
        return ResultBody.success(joinTableService.findById(id));
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids  查询多个Id
     * @return  返回List结果集
     */
    @PostMapping(value = "/findByIds")
    @Operation(summary = "传入多个Id查询数据", description = "")
    public ResultBody findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(joinTableService.findByIds(ids));
    }

    /**
     * 根据查询条件查询列表
     *
     * @param joinTableQuery 查询条件
     * @return  返回List结果集
     */
    @PostMapping(value = "/findByList")
    @Operation(summary = "根据查询条件查询列表", description = "")
    public ResultBody findByList(JoinTableQuery joinTableQuery) {
        return ResultBody.success(joinTableService.findByList(joinTableQuery));
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids  删除多个Id
     * @return  返回是否成功
     */
    @PostMapping(value = "/removeByIds")
    @Operation(summary = "传入多个Id并删除", description = "")
    public ResultBody removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(joinTableService.removeByIds(ids));
    }

    /**
     * 数据分页查询
     *
     * @param joinTableQuery  分页查询条件
     * @param pageParam    分页参数
     * @return  返回分页结果
     */
    @PostMapping(value = "/queryPage")
    @Operation(summary = "数据分页查询", description = "")
    public ResultBody queryPage(@ParameterObject JoinTableQuery joinTableQuery, @ParameterObject PageParam pageParam) {
        return ResultBody.success(joinTableService.queryPage(joinTableQuery, pageParam));
    }

}