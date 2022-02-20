package com.tianque.scgrid.service.issue.focus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.tianque.scgrid.service.issue.focus.param.IssueFocusTagParam;
import com.tianque.scgrid.service.issue.focus.query.IssueFocusTagQuery;
import com.tianque.scgrid.service.issue.focus.service.IssueFocusTagService;
import com.tianque.scgrid.service.issue.focus.vo.IssueFocusTagVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liulei
 */
@RestController
@RequestMapping("/issueFocusTag")
@Api(value = "事件重点类型关注", tags = "事件重点类型关注")
public class IssueFocusTagController {

    private IssueFocusTagService issueFocusTagService;

    /**
     * 使用构造方法注入
     *
     * @param issueFocusTagService
     */
    public IssueFocusTagController(IssueFocusTagService issueFocusTagService){
        this.issueFocusTagService= issueFocusTagService;
    }

    /**
     * 保存对象
     *
     * @param issueFocusTagParam
     * @return
     */
    @PostMapping(value = "/save")
    @ApiOperation(value = "保存", notes = "事件重点类型关注")
    public Boolean save(IssueFocusTagParam issueFocusTagParam) {
        return issueFocusTagService.save(issueFocusTagParam);
    }

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/findById")
    @ApiOperation(value = "通过Id查询数据", notes = "事件重点类型关注")
    public IssueFocusTagVo findById(Long id) {
        return issueFocusTagService.findById(id);
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/findByIds")
    @ApiOperation(value = "传入多个Id查询数据", notes = "事件重点类型关注")
    public List<IssueFocusTagVo> findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return issueFocusTagService.findByIds(ids);
    }


    /**
     * 根据查询条件查询列表
     *
     * @param issueFocusTagQuery
     * @return
     */
    @PostMapping(value = "/findByList")
    @ApiOperation(value = "根据查询条件查询列表", notes = "事件重点类型关注")
    public List<IssueFocusTagVo> findByList(IssueFocusTagQuery issueFocusTagQuery) {
        return issueFocusTagService.findByList(issueFocusTagQuery);
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/removeByIds")
    @ApiOperation(value = "传入多个Id并删除", notes = "事件重点类型关注")
    public Boolean removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return issueFocusTagService.removeByIds(ids);
    }

    /**
     * 数据分页查询
     *
     * @param issueFocusTagQuery
     * @param pageParam
     * @return
     */
    @PostMapping(value = "/queryPage")
    @ApiOperation(value = "数据分页查询", notes = "事件重点类型关注")
    public IPage<IssueFocusTagVo> queryPage(IssueFocusTagQuery issueFocusTagQuery, PageParam pageParam) {
        return issueFocusTagService.queryPage(issueFocusTagQuery, pageParam);
    }

}
