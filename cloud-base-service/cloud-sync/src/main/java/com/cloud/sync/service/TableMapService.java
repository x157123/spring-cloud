package com.cloud.sync.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.sync.param.tableMapParam;
import com.cloud.sync.query.tableMapQuery;
import com.cloud.sync.vo.tableMapVo;

import java.util.List;

/**
 * @author liulei
 */
public interface tableMapService {

    /**
     * 保存对象
     *
     * @param tableMapParam  前端传入对象
     * @return  返回保存成功状态
     */
    Boolean save(tableMapParam tableMapParam);

    /**
     * 通过Id查询数据
     *
     * @param id   业务Id
     * @return  返回VO对象
     */
    tableMapVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids  多个id
     * @return  返回list结果
     */
    List<tableMapVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param tableMapQuery 查询条件
     * @return  返回list结果
     */
    List<tableMapVo> findByList(tableMapQuery tableMapQuery);

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
    IPage<tableMapVo> queryPage(tableMapQuery tableMapQuery, PageParam pageParam);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param writeConnectIds  查询结果集
     * @return 返回结果
     */
    List<tableMapVo> findBywriteConnectId(List<Long> writeConnectIds);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param writeTableIds  查询结果集
     * @return 返回结果
     */
    List<tableMapVo> findBywriteTableId(List<Long> writeTableIds);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param readTableIds  查询结果集
     * @return 返回结果
     */
    List<tableMapVo> findByreadTableId(List<Long> readTableIds);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param readConnectIds  查询结果集
     * @return 返回结果
     */
    List<tableMapVo> findByreadConnectId(List<Long> readConnectIds);
}
