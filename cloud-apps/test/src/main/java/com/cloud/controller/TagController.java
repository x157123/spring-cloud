package com.cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.param.TagParam;
import com.cloud.query.TagQuery;
import com.cloud.service.TagService;
import com.cloud.vo.TagVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liulei
 */
@RestController
@RequestMapping("/tag")
@Api(value = "标签", tags = "标签")
@AllArgsConstructor
public class TagController {

    private TagService tagService;

    /**
     * 保存对象
     *
     * @param tagParam
     * @return
     */
    @PostMapping(value = "/save")
    @ApiOperation(value = "保存", notes = "标签")
    public Boolean save(TagParam tagParam) {
        return tagService.save(tagParam);
    }

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/findById")
    @ApiOperation(value = "通过Id查询数据", notes = "标签")
    public TagVo findById(Long id) {
        return tagService.findById(id);
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/findByIds")
    @ApiOperation(value = "传入多个Id查询数据", notes = "标签")
    public List<TagVo> findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return tagService.findByIds(ids);
    }


    /**
     * 根据查询条件查询列表
     *
     * @param tagQuery
     * @return
     */
    @PostMapping(value = "/findByList")
    @ApiOperation(value = "根据查询条件查询列表", notes = "标签")
    public List<TagVo> findByList(TagQuery tagQuery) {
        return tagService.findByList(tagQuery);
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/removeByIds")
    @ApiOperation(value = "传入多个Id并删除", notes = "标签")
    public Boolean removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return tagService.removeByIds(ids);
    }

    /**
     * 数据分页查询
     *
     * @param tagQuery
     * @param pageParam
     * @return
     */
    @PostMapping(value = "/queryPage")
    @ApiOperation(value = "数据分页查询", notes = "标签")
    public IPage<TagVo> queryPage(TagQuery tagQuery, PageParam pageParam) {
        return tagService.queryPage(tagQuery, pageParam);
    }

}
