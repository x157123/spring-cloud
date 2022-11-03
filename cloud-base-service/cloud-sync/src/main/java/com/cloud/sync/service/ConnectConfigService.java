package com.cloud.sync.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.sync.param.ConnectConfigParam;
import com.cloud.sync.query.ConnectConfigQuery;
import com.cloud.sync.vo.ConnectConfigVo;

import java.util.List;

/**
 * @author liulei
 */
public interface ConnectConfigService {

    /**
     * 保存对象
     *
     * @param connectConfigParam
     * @return
     */
    Boolean save(ConnectConfigParam connectConfigParam);

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    ConnectConfigVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids
     * @return
     */
    List<ConnectConfigVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param connectConfigQuery
     * @return
     */
    List<ConnectConfigVo> findByList(ConnectConfigQuery connectConfigQuery);

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
     * @param connectConfigQuery
     * @param pageParam
     * @return
     */
    IPage<ConnectConfigVo> queryPage(ConnectConfigQuery connectConfigQuery, PageParam pageParam);
}
