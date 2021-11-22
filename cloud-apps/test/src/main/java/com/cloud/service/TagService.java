package com.cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.param.TagParam;
import com.cloud.query.TagQuery;
import com.cloud.vo.TagVo;

import java.util.List;

/**
 * @author liulei
 */
public interface TagService {

    /**
     * 保存对象
     *
     * @param tagParam
     * @return
     */
    Boolean save(TagParam tagParam);

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    TagVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids
     * @return
     */
    List<TagVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param tagQuery
     * @return
     */
    List<TagVo> findByList(TagQuery tagQuery);

    /**
     * 传入多个Id 并删除
     *
     * @param ids
     * @return
     */
    Boolean removeByIds(List<Long> ids);

    /**
     * 数据分页查询
     *
     * @param tagQuery
     * @param pageParam
     * @return
     */
    IPage<TagVo> queryPage(TagQuery tagQuery, PageParam pageParam);
}
