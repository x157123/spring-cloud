package com.cloud.sync.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.sync.param.ServeParam;
import com.cloud.sync.query.ServeQuery;
import com.cloud.sync.vo.ServeVo;

import java.util.List;

/**
 * @author liulei
 */
public interface ServeService {

    /**
     * 保存对象
     *
     * @param serveParam 前端传入对象
     * @return 返回保存成功状态
     */
    Boolean save(ServeParam serveParam);


    /**
     * 通过Id查询  用于页面显示
     *
     * @param id
     * @return
     */
    ServeVo findServeParamById(Long id);

    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    ServeVo findById(Long id);


    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return 返回list结果
     */
    List<ServeVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param serveQuery 查询条件
     * @return 返回list结果
     */
    List<ServeVo> findByList(ServeQuery serveQuery);

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
     * @param serveQuery 查询条件
     * @param pageParam  分页条件
     * @return 分页数据
     */
    IPage<ServeVo> queryPage(ServeQuery serveQuery, PageParam pageParam);

    /**
     * 传入多个业务Id 查询数据
     *
     * @param readConnectIds 查询结果集
     * @return 返回结果
     */
    List<ServeVo> findByReadConnectId(List<Long> readConnectIds);

    /**
     * 传入多个业务Id 查询数据
     *
     * @param writeConnectIds 查询结果集
     * @return 返回结果
     */
    List<ServeVo> findByWriteConnectId(List<Long> writeConnectIds);
}