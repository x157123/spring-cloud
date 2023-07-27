package com.cloud.sync.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.result.ResultBody;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.sync.param.ServeParam;
import com.cloud.sync.query.ServeQuery;
import com.cloud.sync.service.ServeService;
import com.cloud.sync.vo.ServeVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liulei
 */
@RestController
@RequestMapping("/serve")
@Tag(description = "表映射", name = "表映射")
public class ServeController {

    private final ServeService serveService;

    /**
     * 使用构造方法注入
     *
     * @param serveService    注册表映射
     */
    public ServeController(ServeService serveService){
        this.serveService= serveService;
    }

    /**
     * 保存对象
     *
     * @param serveParam  表映射参数
     * @return  返回是否成功
     */
    @PostMapping(value = "/save")
    @Operation(summary = "保存", description = "表映射")
    public ResultBody save(@RequestBody ServeParam serveParam) {
        return ResultBody.success(serveService.save(serveParam));
    }

    /**
     * 通过Id查询数据
     *
     * @param id   查询ID
     * @return  返回查询结果
     */
    @GetMapping(value = "/findById")
    @Operation(summary = "通过Id查询数据", description = "表映射")
    public ResultBody findById(Long id) {
        return ResultBody.success(serveService.findById(id));
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids  查询多个Id
     * @return  返回List结果集
     */
    @PostMapping(value = "/findByIds")
    @Operation(summary = "传入多个Id查询数据", description = "表映射")
    public ResultBody findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(serveService.findByIds(ids));
    }

    /**
     * 根据查询条件查询列表
     *
     * @param serveQuery 查询条件
     * @return  返回List结果集
     */
    @PostMapping(value = "/findByList")
    @Operation(summary = "根据查询条件查询列表", description = "表映射")
    public ResultBody findByList(ServeQuery serveQuery) {
        return ResultBody.success(serveService.findByList(serveQuery));
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids  删除多个Id
     * @return  返回是否成功
     */
    @PostMapping(value = "/removeByIds")
    @Operation(summary = "传入多个Id并删除", description = "表映射")
    public ResultBody removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return ResultBody.success(serveService.removeByIds(ids));
    }

    /**
     * 数据分页查询
     *
     * @param serveQuery  分页查询条件
     * @param pageParam    分页参数
     * @return  返回分页结果
     */
    @PostMapping(value = "/queryPage")
    @Operation(summary = "数据分页查询", description = "表映射")
    public ResultBody queryPage(@ParameterObject ServeQuery serveQuery, @ParameterObject PageParam pageParam) {
        return ResultBody.success(serveService.queryPage(serveQuery, pageParam));
    }

}