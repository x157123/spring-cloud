package com.cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.param.MenuParam;
import com.cloud.query.MenuQuery;
import com.cloud.service.MenuService;
import com.cloud.vo.MenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liulei
 */
@RestController
@RequestMapping("/menu")
@Api(value = "菜单权限", tags = "菜单权限")
@AllArgsConstructor
public class MenuController {

    private MenuService menuService;

    /**
     * 保存对象
     *
     * @param menuParam
     * @return
     */
    @PostMapping(value = "/save")
    @ApiOperation(value = "保存", notes = "菜单权限")
    public Boolean save(MenuParam menuParam) {
        return menuService.save(menuParam);
    }

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/findById")
    @ApiOperation(value = "通过Id查询数据", notes = "菜单权限")
    public MenuVo findById(Long id) {
        return menuService.findById(id);
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/findByIds")
    @ApiOperation(value = "传入多个Id查询数据", notes = "菜单权限")
    public List<MenuVo> findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return menuService.findByIds(ids);
    }


    /**
     * 根据查询条件查询列表
     *
     * @param menuQuery
     * @return
     */
    @PostMapping(value = "/findByList")
    @ApiOperation(value = "根据查询条件查询列表", notes = "菜单权限")
    public List<MenuVo> findByList(MenuQuery menuQuery) {
        return menuService.findByList(menuQuery);
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/removeByIds")
    @ApiOperation(value = "传入多个Id并删除", notes = "菜单权限")
    public Boolean removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return menuService.removeByIds(ids);
    }

    /**
     * 数据分页查询
     *
     * @param menuQuery
     * @param pageParam
     * @return
     */
    @PostMapping(value = "/queryPage")
    @ApiOperation(value = "数据分页查询", notes = "菜单权限")
    public IPage<MenuVo> queryPage(MenuQuery menuQuery, PageParam pageParam) {
        return menuService.queryPage(menuQuery, pageParam);
    }

}
