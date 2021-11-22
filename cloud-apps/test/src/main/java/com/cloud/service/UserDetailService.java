package com.cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.param.UserDetailParam;
import com.cloud.query.UserDetailQuery;
import com.cloud.vo.UserDetailVo;

import java.util.List;

/**
 * @author liulei
 */
public interface UserDetailService {

    /**
     * 保存对象
     *
     * @param userDetailParam
     * @return
     */
    Boolean save(UserDetailParam userDetailParam);

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    UserDetailVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids
     * @return
     */
    List<UserDetailVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param userDetailQuery
     * @return
     */
    List<UserDetailVo> findByList(UserDetailQuery userDetailQuery);

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
     * @param userDetailQuery
     * @param pageParam
     * @return
     */
    IPage<UserDetailVo> queryPage(UserDetailQuery userDetailQuery, PageParam pageParam);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param userIds
     * @return
     */
    List<UserDetailVo> findByUserId(List<Long> userIds);
}
