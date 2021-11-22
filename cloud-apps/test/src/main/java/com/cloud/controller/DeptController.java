package com.cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.param.DeptParam;
import com.cloud.query.DeptQuery;
import com.cloud.service.DeptService;
import com.cloud.vo.DeptVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liulei
 */
@RestController
@RequestMapping("/dept")
@Api(value = "部门", tags = "部门")
@AllArgsConstructor
public class DeptController {

    private DeptService deptService;

    /**
     * 保存对象
     *
     * @param deptParam
     * @return
     */
    @PostMapping(value = "/save")
    @ApiOperation(value = "保存", notes = "部门")
    public Boolean save(DeptParam deptParam) {
        return deptService.save(deptParam);
    }

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/findById")
    @ApiOperation(value = "通过Id查询数据", notes = "部门")
    public DeptVo findById(Long id) {
        return deptService.findById(id);
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/findByIds")
    @ApiOperation(value = "传入多个Id查询数据", notes = "部门")
    public List<DeptVo> findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return deptService.findByIds(ids);
    }


    /**
     * 根据查询条件查询列表
     *
     * @param deptQuery
     * @return
     */
    @PostMapping(value = "/findByList")
    @ApiOperation(value = "根据查询条件查询列表", notes = "部门")
    public List<DeptVo> findByList(DeptQuery deptQuery) {
        return deptService.findByList(deptQuery);
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/removeByIds")
    @ApiOperation(value = "传入多个Id并删除", notes = "部门")
    public Boolean removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return deptService.removeByIds(ids);
    }

    /**
     * 数据分页查询
     *
     * @param deptQuery
     * @param pageParam
     * @return
     */
    @PostMapping(value = "/queryPage")
    @ApiOperation(value = "数据分页查询", notes = "部门")
    public IPage<DeptVo> queryPage(DeptQuery deptQuery, PageParam pageParam) {
        return deptService.queryPage(deptQuery, pageParam);
    }

}
