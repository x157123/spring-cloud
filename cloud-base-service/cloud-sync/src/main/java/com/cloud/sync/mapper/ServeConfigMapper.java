package com.cloud.sync.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.sync.entity.ServeConfig;
import com.cloud.sync.query.ServeConfigQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface ServeConfigMapper extends BaseMapper<ServeConfig> {

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @param serveConfigQuery 查询条件
     * @return  分页数据
     */
    IPage<ServeConfig> queryPage(IPage<ServeConfig> page, @Param("param") ServeConfigQuery serveConfigQuery);

}
