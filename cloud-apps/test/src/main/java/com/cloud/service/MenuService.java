package com.cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.param.MenuParam;
import com.cloud.query.MenuQuery;
import com.cloud.vo.MenuVo;

import java.util.List;

/**
 * @author liulei
 */
public interface MenuService {

    /**
     * 保存对象
     *
     * @param menuParam
     * @return
     */
    Boolean save(MenuParam menuParam);

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    MenuVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids
     * @return
     */
    List<MenuVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param menuQuery
     * @return
     */
    List<MenuVo> findByList(MenuQuery menuQuery);

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
     * @param menuQuery
     * @param pageParam
     * @return
     */
    IPage<MenuVo> queryPage(MenuQuery menuQuery, PageParam pageParam);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param nodeIds
     * @return
     */
    List<MenuVo> findByNodeId(List<Long> nodeIds);
}
