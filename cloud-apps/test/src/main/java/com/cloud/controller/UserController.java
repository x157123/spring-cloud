package com.cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.param.UserParam;
import com.cloud.query.UserQuery;
import com.cloud.service.UserService;
import com.cloud.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liulei
 */
@RestController
@RequestMapping("/user")
@Api(value = "用户信息", tags = "用户信息")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    /**
     * 保存对象
     *
     * @param userParam
     * @return
     */
    @PostMapping(value = "/save")
    @ApiOperation(value = "保存", notes = "用户信息")
    public Boolean save(UserParam userParam) {
        return userService.save(userParam);
    }

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/findById")
    @ApiOperation(value = "通过Id查询数据", notes = "用户信息")
    public UserVo findById(Long id) {
        return userService.findById(id);
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/findByIds")
    @ApiOperation(value = "传入多个Id查询数据", notes = "用户信息")
    public List<UserVo> findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return userService.findByIds(ids);
    }


    /**
     * 根据查询条件查询列表
     *
     * @param userQuery
     * @return
     */
    @PostMapping(value = "/findByList")
    @ApiOperation(value = "根据查询条件查询列表", notes = "用户信息")
    public List<UserVo> findByList(UserQuery userQuery) {
        return userService.findByList(userQuery);
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/removeByIds")
    @ApiOperation(value = "传入多个Id并删除", notes = "用户信息")
    public Boolean removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return userService.removeByIds(ids);
    }

    /**
     * 数据分页查询
     *
     * @param userQuery
     * @param pageParam
     * @return
     */
    @PostMapping(value = "/queryPage")
    @ApiOperation(value = "数据分页查询", notes = "用户信息")
    public IPage<UserVo> queryPage(UserQuery userQuery, PageParam pageParam) {
        return userService.queryPage(userQuery, pageParam);
    }

}
