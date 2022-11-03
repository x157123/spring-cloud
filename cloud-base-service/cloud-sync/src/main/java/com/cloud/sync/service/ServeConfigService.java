package com.cloud.sync.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.sync.param.ServeConfigParam;
import com.cloud.sync.query.ServeConfigQuery;
import com.cloud.sync.vo.ServeConfigVo;

import java.util.List;

/**
 * @author liulei
 */
public interface ServeConfigService {

    /**
     * 保存对象
     *
     * @param serveConfigParam
     * @return
     */
    Boolean save(ServeConfigParam serveConfigParam);

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    ServeConfigVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids
     * @return
     */
    List<ServeConfigVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param serveConfigQuery
     * @return
     */
    List<ServeConfigVo> findByList(ServeConfigQuery serveConfigQuery);

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
     * @param serveConfigQuery
     * @param pageParam
     * @return
     */
    IPage<ServeConfigVo> queryPage(ServeConfigQuery serveConfigQuery, PageParam pageParam);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param readConnectIds
     * @return
     */
    List<ServeConfigVo> findByReadConnectId(List<Long> readConnectIds);
}
