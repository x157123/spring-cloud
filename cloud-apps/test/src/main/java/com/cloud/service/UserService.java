package com.cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.param.UserParam;
import com.cloud.query.UserQuery;
import com.cloud.vo.UserVo;

import java.util.List;

/**
 * @author liulei
 */
public interface UserService {

    /**
     * 保存对象
     *
     * @param userParam
     * @return
     */
    Boolean save(UserParam userParam);

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    UserVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids
     * @return
     */
    List<UserVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param userQuery
     * @return
     */
    List<UserVo> findByList(UserQuery userQuery);

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
     * @param userQuery
     * @param pageParam
     * @return
     */
    IPage<UserVo> queryPage(UserQuery userQuery, PageParam pageParam);
}
