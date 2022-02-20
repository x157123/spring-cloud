package com.tianque.scgrid.service.issue.link.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.tianque.scgrid.service.issue.link.param.IssueLinkPersonParam;
import com.tianque.scgrid.service.issue.link.query.IssueLinkPersonQuery;
import com.tianque.scgrid.service.issue.link.vo.IssueLinkPersonVo;

import java.util.List;

/**
 * @author liulei
 */
public interface IssueLinkPersonService {

    /**
     * 保存对象
     *
     * @param issueLinkPersonParam
     * @return
     */
    Boolean save(IssueLinkPersonParam issueLinkPersonParam);

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    IssueLinkPersonVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids
     * @return
     */
    List<IssueLinkPersonVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param issueLinkPersonQuery
     * @return
     */
    List<IssueLinkPersonVo> findByList(IssueLinkPersonQuery issueLinkPersonQuery);

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
     * @param issueLinkPersonQuery
     * @param pageParam
     * @return
     */
    IPage<IssueLinkPersonVo> queryPage(IssueLinkPersonQuery issueLinkPersonQuery, PageParam pageParam);
}
