package com.cloud.sync.base.service;

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
     * @param columnConfigParams 前端传入对象
     * @return 返回保存成功状态
     */
    Boolean save(List<ColumnConfigParam> columnConfigParams);

    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    ColumnConfigVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return 返回list结果
     */
    List<ColumnConfigVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param columnConfigQuery 查询条件
     * @return 返回list结果
     */
    List<ColumnConfigVo> findByList(ColumnConfigQuery columnConfigQuery);

    /**
     * 传入多个Id 并删除
     *
     * @param ids id集合
     * @return 删除情况状态
     */
    Boolean removeByIds(List<Long> ids);

    /**
     * 数据分页查询
     *
     * @param columnConfigQuery 查询条件
     * @param pageParam         分页条件
     * @return 分页数据
     */
    IPage<ColumnConfigVo> queryPage(ColumnConfigQuery columnConfigQuery, PageParam pageParam);

    /**
     * 传入多个业务Id 查询数据
     *
     * @param tableIds 查询结果集
     * @return 返回结果
     */
    List<ColumnConfigVo> findByTableId(List<Long> tableIds);
}