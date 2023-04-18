package com.cloud.sync.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.sync.param.serveConfigParam;
import com.cloud.sync.query.serveConfigQuery;
import com.cloud.sync.vo.serveConfigVo;

import java.util.List;

/**
 * @author liulei
 */
public interface serveConfigService {

    /**
     * 保存对象
     *
     * @param serveConfigParam  前端传入对象
     * @return  返回保存成功状态
     */
    Boolean save(serveConfigParam serveConfigParam);

    /**
     * 通过Id查询数据
     *
     * @param id   业务Id
     * @return  返回VO对象
     */
    serveConfigVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids  多个id
     * @return  返回list结果
     */
    List<serveConfigVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param serveConfigQuery 查询条件
     * @return  返回list结果
     */
    List<serveConfigVo> findByList(serveConfigQuery serveConfigQuery);

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
     * @param serveConfigQuery 查询条件
     * @param pageParam    分页条件
     * @return 分页数据
     */
    IPage<serveConfigVo> queryPage(serveConfigQuery serveConfigQuery, PageParam pageParam);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param readConnectIds  查询结果集
     * @return 返回结果
     */
    List<serveConfigVo> findByreadConnectId(List<Long> readConnectIds);
}
