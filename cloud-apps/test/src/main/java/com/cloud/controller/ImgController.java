package com.cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.param.ImgParam;
import com.cloud.query.ImgQuery;
import com.cloud.service.ImgService;
import com.cloud.vo.ImgVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liulei
 */
@RestController
@RequestMapping("/img")
@Api(value = "照片", tags = "照片")
@AllArgsConstructor
public class ImgController {

    private ImgService imgService;

    /**
     * 保存对象
     *
     * @param imgParam
     * @return
     */
    @PostMapping(value = "/save")
    @ApiOperation(value = "保存", notes = "照片")
    public Boolean save(ImgParam imgParam) {
        return imgService.save(imgParam);
    }

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/findById")
    @ApiOperation(value = "通过Id查询数据", notes = "照片")
    public ImgVo findById(Long id) {
        return imgService.findById(id);
    }

    /**
     * 传入多个Id查询数据
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/findByIds")
    @ApiOperation(value = "传入多个Id查询数据", notes = "照片")
    public List<ImgVo> findByIds(@RequestParam(value = "ids") List<Long> ids) {
        return imgService.findByIds(ids);
    }


    /**
     * 根据查询条件查询列表
     *
     * @param imgQuery
     * @return
     */
    @PostMapping(value = "/findByList")
    @ApiOperation(value = "根据查询条件查询列表", notes = "照片")
    public List<ImgVo> findByList(ImgQuery imgQuery) {
        return imgService.findByList(imgQuery);
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/removeByIds")
    @ApiOperation(value = "传入多个Id并删除", notes = "照片")
    public Boolean removeByIds(@RequestParam(value = "ids") List<Long> ids) {
        return imgService.removeByIds(ids);
    }

    /**
     * 数据分页查询
     *
     * @param imgQuery
     * @param pageParam
     * @return
     */
    @PostMapping(value = "/queryPage")
    @ApiOperation(value = "数据分页查询", notes = "照片")
    public IPage<ImgVo> queryPage(ImgQuery imgQuery, PageParam pageParam) {
        return imgService.queryPage(imgQuery, pageParam);
    }

}
