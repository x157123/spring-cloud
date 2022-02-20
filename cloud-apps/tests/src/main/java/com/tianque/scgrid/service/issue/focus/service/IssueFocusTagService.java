package com.tianque.scgrid.service.issue.focus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.tianque.scgrid.service.issue.focus.param.IssueFocusTagParam;
import com.tianque.scgrid.service.issue.focus.query.IssueFocusTagQuery;
import com.tianque.scgrid.service.issue.focus.vo.IssueFocusTagVo;

import java.util.List;

/**
 * @author liulei
 */
public interface IssueFocusTagService {

    /**
     * 保存对象
     *
     * @param issueFocusTagParam
     * @return
     */
    Boolean save(IssueFocusTagParam issueFocusTagParam);

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    IssueFocusTagVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids
     * @return
     */
    List<IssueFocusTagVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param issueFocusTagQuery
     * @return
     */
    List<IssueFocusTagVo> findByList(IssueFocusTagQuery issueFocusTagQuery);

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
     * @param issueFocusTagQuery
     * @param pageParam
     * @return
     */
    IPage<IssueFocusTagVo> queryPage(IssueFocusTagQuery issueFocusTagQuery, PageParam pageParam);
}
