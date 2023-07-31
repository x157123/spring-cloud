package com.cloud.sync.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.sync.param.JoinTableParam;
import com.cloud.sync.query.JoinTableQuery;
import com.cloud.sync.vo.JoinTableVo;

import java.util.List;

/**
 * @author liulei
 */
public interface JoinTableService {

    /**
     * 保存对象
     *
     * @param joinTableParam  前端传入对象
     * @return  返回保存成功状态
     */
    Boolean save(JoinTableParam joinTableParam);

    /**
     * 通过Id查询数据
     *
     * @param id   业务Id
     * @return  返回VO对象
     */
    JoinTableVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids  多个id
     * @return  返回list结果
     */
    List<JoinTableVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param joinTableQuery 查询条件
     * @return  返回list结果
     */
    List<JoinTableVo> findByList(JoinTableQuery joinTableQuery);

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
     * @param joinTableQuery 查询条件
     * @param pageParam    分页条件
     * @return 分页数据
     */
    IPage<JoinTableVo> queryPage(JoinTableQuery joinTableQuery, PageParam pageParam);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param connectIds  查询结果集
     * @return 返回结果
     */
    List<JoinTableVo> findByConnectId(List<Long> connectIds);
}