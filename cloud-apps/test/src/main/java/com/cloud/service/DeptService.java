package com.cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.param.DeptParam;
import com.cloud.query.DeptQuery;
import com.cloud.vo.DeptVo;

import java.util.List;

/**
 * @author liulei
 */
public interface DeptService {

    /**
     * 保存对象
     *
     * @param deptParam
     * @return
     */
    Boolean save(DeptParam deptParam);

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    DeptVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids
     * @return
     */
    List<DeptVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param deptQuery
     * @return
     */
    List<DeptVo> findByList(DeptQuery deptQuery);

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
     * @param deptQuery
     * @param pageParam
     * @return
     */
    IPage<DeptVo> queryPage(DeptQuery deptQuery, PageParam pageParam);
}
