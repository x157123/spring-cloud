package com.cloud.sync.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.sync.param.ServeTableParam;
import com.cloud.sync.query.ServeTableQuery;
import com.cloud.sync.vo.ServeTableVo;

import java.util.List;

/**
 * @author liulei
 */
public interface ServeTableService {

    /**
     * 保存对象
     *
     * @param serveTableParam
     * @return
     */
    Boolean save(ServeTableParam serveTableParam);

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    ServeTableVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids
     * @return
     */
    List<ServeTableVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param serveTableQuery
     * @return
     */
    List<ServeTableVo> findByList(ServeTableQuery serveTableQuery);

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
     * @param serveTableQuery
     * @param pageParam
     * @return
     */
    IPage<ServeTableVo> queryPage(ServeTableQuery serveTableQuery, PageParam pageParam);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param serveIds
     * @return
     */
    List<ServeTableVo> findByServeId(List<Long> serveIds);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param tableMapIds
     * @return
     */
    List<ServeTableVo> findByTableMapId(List<Long> tableMapIds);
}
