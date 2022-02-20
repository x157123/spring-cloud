package com.tianque.scgrid.service.issue.link.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.tianque.scgrid.service.issue.link.param.IssueLinkPersonParam;
import com.tianque.scgrid.service.issue.link.query.IssueLinkPersonQuery;
import com.tianque.scgrid.service.issue.link.service.IssueLinkPersonService;
import com.tianque.scgrid.service.issue.link.vo.IssueLinkPersonVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liulei
 */
@RestController
@RequestMapping("/issueLinkPerson")
@Api(value = "事件人员关联", tags = "事件人员关联")
public class IssueLinkPersonController {

    private IssueLinkPersonService issueLinkPersonService;

    /**
     * 使用构造方法注入
     *
     * @param issueLinkPersonService
     */
    public IssueLinkPersonController(IssueLinkPersonService issueLinkPersonService){
        this.issueLinkPersonService= issueLinkPersonService;
    }

    /**
     * 保存对象
     *
     * @param issueLinkPersonParam
     * @return
     */
    @PostMapping(value = "/save")
    @ApiOperation(value = "保存", notes = "事件人员关联")
    public Boolean save(IssueLinkPersonParam issueLinkPersonParam) {
        return issueLinkPersonService.save(issueLinkPersonParam);
    }

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/findById")
    @ApiOperation(value = "通过Id查询数据", notes = "事件人员关联")
    public IssueLinkPersonVo findById(Long id) {
        return issueLinkPersonService.findById(id);
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/findByIds")
    @ApiOperation(value = "传入多个Id查询数据", notes = "事件人员关联")
    public List<IssueLinkPersonVo> findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return issueLinkPersonService.findByIds(ids);
    }


    /**
     * 根据查询条件查询列表
     *
     * @param issueLinkPersonQuery
     * @return
     */
    @PostMapping(value = "/findByList")
    @ApiOperation(value = "根据查询条件查询列表", notes = "事件人员关联")
    public List<IssueLinkPersonVo> findByList(IssueLinkPersonQuery issueLinkPersonQuery) {
        return issueLinkPersonService.findByList(issueLinkPersonQuery);
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/removeByIds")
    @ApiOperation(value = "传入多个Id并删除", notes = "事件人员关联")
    public Boolean removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return issueLinkPersonService.removeByIds(ids);
    }

    /**
     * 数据分页查询
     *
     * @param issueLinkPersonQuery
     * @param pageParam
     * @return
     */
    @PostMapping(value = "/queryPage")
    @ApiOperation(value = "数据分页查询", notes = "事件人员关联")
    public IPage<IssueLinkPersonVo> queryPage(IssueLinkPersonQuery issueLinkPersonQuery, PageParam pageParam) {
        return issueLinkPersonService.queryPage(issueLinkPersonQuery, pageParam);
    }

}
