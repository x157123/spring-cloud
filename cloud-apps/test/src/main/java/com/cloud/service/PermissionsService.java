package com.cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.param.PermissionsParam;
import com.cloud.query.PermissionsQuery;
import com.cloud.vo.PermissionsVo;

import java.util.List;

/**
 * @author liulei
 */
public interface PermissionsService {

    /**
     * 保存对象
     *
     * @param permissionsParam
     * @return
     */
    Boolean save(PermissionsParam permissionsParam);

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    PermissionsVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids
     * @return
     */
    List<PermissionsVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param permissionsQuery
     * @return
     */
    List<PermissionsVo> findByList(PermissionsQuery permissionsQuery);

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
     * @param permissionsQuery
     * @param pageParam
     * @return
     */
    IPage<PermissionsVo> queryPage(PermissionsQuery permissionsQuery, PageParam pageParam);
}
