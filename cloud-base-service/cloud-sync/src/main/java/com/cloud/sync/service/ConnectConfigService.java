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
     * @param connectConfigParam  前端传入对象
     * @return  返回保存成功状态
     */
    Boolean save(ConnectConfigParam connectConfigParam);

    /**
     * 通过Id查询数据
     *
     * @param id   业务Id
     * @return  返回VO对象
     */
    ConnectConfigVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids  多个id
     * @return  返回list结果
     */
    List<ConnectConfigVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param connectConfigQuery 查询条件
     * @return  返回list结果
     */
    List<ConnectConfigVo> findByList(ConnectConfigQuery connectConfigQuery);

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
     * @param connectConfigQuery 查询条件
     * @param pageParam    分页条件
     * @return 分页数据
     */
    IPage<ConnectConfigVo> queryPage(ConnectConfigQuery connectConfigQuery, PageParam pageParam);
}