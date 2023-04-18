package com.cloud.sync.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.sync.param.connectConfigParam;
import com.cloud.sync.query.connectConfigQuery;
import com.cloud.sync.vo.connectConfigVo;

import java.util.List;

/**
 * @author liulei
 */
public interface connectConfigService {

    /**
     * 保存对象
     *
     * @param connectConfigParam  前端传入对象
     * @return  返回保存成功状态
     */
    Boolean save(connectConfigParam connectConfigParam);

    /**
     * 通过Id查询数据
     *
     * @param id   业务Id
     * @return  返回VO对象
     */
    connectConfigVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids  多个id
     * @return  返回list结果
     */
    List<connectConfigVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param connectConfigQuery 查询条件
     * @return  返回list结果
     */
    List<connectConfigVo> findByList(connectConfigQuery connectConfigQuery);

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
    IPage<connectConfigVo> queryPage(connectConfigQuery connectConfigQuery, PageParam pageParam);
}
