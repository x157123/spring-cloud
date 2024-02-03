package com.cloud.sync.base.service;

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
     * @param serveConfigParam 前端传入对象
     * @return 返回保存成功状态
     */
    Boolean save(ServeConfigParam serveConfigParam);

    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    ServeConfigVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return 返回list结果
     */
    List<ServeConfigVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param serveConfigQuery 查询条件
     * @return 返回list结果
     */
    List<ServeConfigVo> findByList(ServeConfigQuery serveConfigQuery);

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
     * @param serveConfigQuery 查询条件
     * @param pageParam        分页条件
     * @return 分页数据
     */
    IPage<ServeConfigVo> queryPage(ServeConfigQuery serveConfigQuery, PageParam pageParam);

    /**
     * 通过服务名获取配置
     *
     * @param serveId
     * @return
     */
    ServeConfigVo findByServerId(Long serveId);

    /**
     * 更新状态
     *
     * @param serveId
     * @param state
     */
    void state(Long serveId, int state);


    /**
     * 传入多个业务Id 查询数据
     *
     * @param serveIds 查询结果集
     * @return 返回结果
     */
    List<ServeConfigVo> findByServeId(List<Long> serveIds);

    /**
     * 删除记录
     *
     * @param serveId
     */
    void removeByServerId(Long serveId);
}