package com.cloud.sync.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.sync.entity.ColumnConfig;
import com.cloud.sync.query.ColumnConfigQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface ColumnConfigMapper extends BaseMapper<ColumnConfig> {

    /**
     * 分页查询
     *
     * @param page
     * @param columnConfigQuery
     * @return
     */
    IPage<ColumnConfig> queryPage(IPage<ColumnConfig> page, @Param("param") ColumnConfigQuery columnConfigQuery);

}
