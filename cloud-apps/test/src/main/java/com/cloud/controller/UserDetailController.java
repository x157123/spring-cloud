package com.cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.param.UserDetailParam;
import com.cloud.query.UserDetailQuery;
import com.cloud.service.UserDetailService;
import com.cloud.vo.UserDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liulei
 */
@RestController
@RequestMapping("/userDetail")
@Api(value = "用户详情", tags = "用户详情")
@AllArgsConstructor
public class UserDetailController {

    private UserDetailService userDetailService;

    /**
     * 保存对象
     *
     * @param userDetailParam
     * @return
     */
    @PostMapping(value = "/save")
    @ApiOperation(value = "保存", notes = "用户详情")
    public Boolean save(UserDetailParam userDetailParam) {
        return userDetailService.save(userDetailParam);
    }

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/findById")
    @ApiOperation(value = "通过Id查询数据", notes = "用户详情")
    public UserDetailVo findById(Long id) {
        return userDetailService.findById(id);
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/findByIds")
    @ApiOperation(value = "传入多个Id查询数据", notes = "用户详情")
    public List<UserDetailVo> findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return userDetailService.findByIds(ids);
    }


    /**
     * 根据查询条件查询列表
     *
     * @param userDetailQuery
     * @return
     */
    @PostMapping(value = "/findByList")
    @ApiOperation(value = "根据查询条件查询列表", notes = "用户详情")
    public List<UserDetailVo> findByList(UserDetailQuery userDetailQuery) {
        return userDetailService.findByList(userDetailQuery);
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/removeByIds")
    @ApiOperation(value = "传入多个Id并删除", notes = "用户详情")
    public Boolean removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return userDetailService.removeByIds(ids);
    }

    /**
     * 数据分页查询
     *
     * @param userDetailQuery
     * @param pageParam
     * @return
     */
    @PostMapping(value = "/queryPage")
    @ApiOperation(value = "数据分页查询", notes = "用户详情")
    public IPage<UserDetailVo> queryPage(UserDetailQuery userDetailQuery, PageParam pageParam) {
        return userDetailService.queryPage(userDetailQuery, pageParam);
    }

}
