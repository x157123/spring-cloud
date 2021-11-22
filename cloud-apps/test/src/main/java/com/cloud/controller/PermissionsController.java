package com.cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.param.PermissionsParam;
import com.cloud.query.PermissionsQuery;
import com.cloud.service.PermissionsService;
import com.cloud.vo.PermissionsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liulei
 */
@RestController
@RequestMapping("/permissions")
@Api(value = "权限", tags = "权限")
@AllArgsConstructor
public class PermissionsController {

    private PermissionsService permissionsService;

    /**
     * 保存对象
     *
     * @param permissionsParam
     * @return
     */
    @PostMapping(value = "/save")
    @ApiOperation(value = "保存", notes = "权限")
    public Boolean save(PermissionsParam permissionsParam) {
        return permissionsService.save(permissionsParam);
    }

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/findById")
    @ApiOperation(value = "通过Id查询数据", notes = "权限")
    public PermissionsVo findById(Long id) {
        return permissionsService.findById(id);
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/findByIds")
    @ApiOperation(value = "传入多个Id查询数据", notes = "权限")
    public List<PermissionsVo> findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return permissionsService.findByIds(ids);
    }


    /**
     * 根据查询条件查询列表
     *
     * @param permissionsQuery
     * @return
     */
    @PostMapping(value = "/findByList")
    @ApiOperation(value = "根据查询条件查询列表", notes = "权限")
    public List<PermissionsVo> findByList(PermissionsQuery permissionsQuery) {
        return permissionsService.findByList(permissionsQuery);
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/removeByIds")
    @ApiOperation(value = "传入多个Id并删除", notes = "权限")
    public Boolean removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return permissionsService.removeByIds(ids);
    }

    /**
     * 数据分页查询
     *
     * @param permissionsQuery
     * @param pageParam
     * @return
     */
    @PostMapping(value = "/queryPage")
    @ApiOperation(value = "数据分页查询", notes = "权限")
    public IPage<PermissionsVo> queryPage(PermissionsQuery permissionsQuery, PageParam pageParam) {
        return permissionsService.queryPage(permissionsQuery, pageParam);
    }

}
