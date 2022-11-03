package com.cloud.sync.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.sync.param.ColumnConfigParam;
import com.cloud.sync.query.ColumnConfigQuery;
import com.cloud.sync.vo.ColumnConfigVo;

import java.util.List;

/**
 * @author liulei
 */
public interface ColumnConfigService {

    /**
     * 保存对象
     *
     * @param columnConfigParam
     * @return
     */
    Boolean save(ColumnConfigParam columnConfigParam);

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    ColumnConfigVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids
     * @return
     */
    List<ColumnConfigVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param columnConfigQuery
     * @return
     */
    List<ColumnConfigVo> findByList(ColumnConfigQuery columnConfigQuery);

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
     * @param columnConfigQuery
     * @param pageParam
     * @return
     */
    IPage<ColumnConfigVo> queryPage(ColumnConfigQuery columnConfigQuery, PageParam pageParam);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param connectIds
     * @return
     */
    List<ColumnConfigVo> findByConnectId(List<Long> connectIds);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param tableIds
     * @return
     */
    List<ColumnConfigVo> findByTableId(List<Long> tableIds);
}
