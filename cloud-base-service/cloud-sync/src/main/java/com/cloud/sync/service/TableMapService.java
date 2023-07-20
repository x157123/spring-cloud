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
     * @param tableMapParam  前端传入对象
     * @return  返回保存成功状态
     */
    Boolean save(TableMapParam tableMapParam);

    /**
     * 通过Id查询数据
     *
     * @param id   业务Id
     * @return  返回VO对象
     */
    TableMapVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids  多个id
     * @return  返回list结果
     */
    List<TableMapVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param tableMapQuery 查询条件
     * @return  返回list结果
     */
    List<TableMapVo> findByList(TableMapQuery tableMapQuery);

    /**
     * 传入多个Id 并删除
     *
     * @param ids  id集合
     * @return  删除情况状态
     */
    Boolean removeByIds(List<Long> ids);

    /**
     * 数据分页查询
     *
     * @param tableMapQuery 查询条件
     * @param pageParam    分页条件
     * @return 分页数据
     */
    IPage<TableMapVo> queryPage(TableMapQuery tableMapQuery, PageParam pageParam);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param readConnectIds  查询结果集
     * @return 返回结果
     */
    List<TableMapVo> findByReadConnectId(List<Long> readConnectIds);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param readTableIds  查询结果集
     * @return 返回结果
     */
    List<TableMapVo> findByReadTableId(List<Long> readTableIds);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param writeConnectIds  查询结果集
     * @return 返回结果
     */
    List<TableMapVo> findByWriteConnectId(List<Long> writeConnectIds);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param writeTableIds  查询结果集
     * @return 返回结果
     */
    List<TableMapVo> findByWriteTableId(List<Long> writeTableIds);
}