package com.cloud.sync.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.sync.param.TableConfigParam;
import com.cloud.sync.query.TableConfigQuery;
import com.cloud.sync.vo.TableConfigVo;

import java.util.List;

/**
 * @author liulei
 */
public interface TableConfigService {

    /**
     * 保存对象
     *
     * @param tableConfigParam
     * @return
     */
    Boolean save(TableConfigParam tableConfigParam);

    /**
     * 通过Id查询数据
     *
     * @param id
     * @return
     */
    TableConfigVo findById(Long id);

    /**
     * 传入多个Id 查询数据
     *
     * @param ids
     * @return
     */
    List<TableConfigVo> findByIds(List<Long> ids);


    /**
     * 根据查询条件 查询列表
     *
     * @param tableConfigQuery
     * @return
     */
    List<TableConfigVo> findByList(TableConfigQuery tableConfigQuery);

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
     * @param tableConfigQuery
     * @param pageParam
     * @return
     */
    IPage<TableConfigVo> queryPage(TableConfigQuery tableConfigQuery, PageParam pageParam);
	
    /**
     * 传入多个业务Id 查询数据
     *
     * @param connectIds
     * @return
     */
    List<TableConfigVo> findByConnectId(List<Long> connectIds);
}
