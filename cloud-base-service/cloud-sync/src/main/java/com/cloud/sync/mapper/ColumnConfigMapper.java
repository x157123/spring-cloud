package com.cloud.sync.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.sync.entity.columnConfig;
import com.cloud.sync.query.columnConfigQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface columnConfigMapper extends BaseMapper<columnConfig> {

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @param columnConfigQuery 查询条件
     * @return  分页数据
     */
    IPage<columnConfig> queryPage(IPage<columnConfig> page, @Param("param") columnConfigQuery columnConfigQuery);

}
