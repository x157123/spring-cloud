package com.cloud.sync.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.sync.param.TableMapParam;
import com.cloud.sync.query.TableMapQuery;
import com.cloud.sync.vo.TableMapVo;

import java.util.List;

/**
 * @author liulei
 */
public interface TableMapService {

    /**
     * 保存对象
     *
     * @param tableMapParam
     * @return
     */
    Boolean save(TableMapParam tableMapParam);

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    TableMapVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids
     * @return
     */
    List<TableMapVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param tableMapQuery
     * @return
     */
    List<TableMapVo> findByList(TableMapQuery tableMapQuery);

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
     * @param tableMapQuery
     * @param pageParam
     * @return
     */
    IPage<TableMapVo> queryPage(TableMapQuery tableMapQuery, PageParam pageParam);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param writeConnectIds
     * @return
     */
    List<TableMapVo> findByWriteConnectId(List<Long> writeConnectIds);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param writeTableIds
     * @return
     */
    List<TableMapVo> findByWriteTableId(List<Long> writeTableIds);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param readTableIds
     * @return
     */
    List<TableMapVo> findByReadTableId(List<Long> readTableIds);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param readConnectIds
     * @return
     */
    List<TableMapVo> findByReadConnectId(List<Long> readConnectIds);
}
