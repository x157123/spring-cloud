package com.cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.param.ImgParam;
import com.cloud.query.ImgQuery;
import com.cloud.vo.ImgVo;

import java.util.List;

/**
 * @author liulei
 */
public interface ImgService {

    /**
     * 保存对象
     *
     * @param imgParam
     * @return
     */
    Boolean save(ImgParam imgParam);

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    ImgVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids
     * @return
     */
    List<ImgVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param imgQuery
     * @return
     */
    List<ImgVo> findByList(ImgQuery imgQuery);

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
     * @param imgQuery
     * @param pageParam
     * @return
     */
    IPage<ImgVo> queryPage(ImgQuery imgQuery, PageParam pageParam);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param userIds
     * @return
     */
    List<ImgVo> findByUserId(List<Long> userIds);
}
