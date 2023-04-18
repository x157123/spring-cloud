package com.cloud.sync.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.sync.entity.serveConfig;
import com.cloud.sync.query.serveConfigQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface serveConfigMapper extends BaseMapper<serveConfig> {

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @param serveConfigQuery 查询条件
     * @return  分页数据
     */
    IPage<serveConfig> queryPage(IPage<serveConfig> page, @Param("param") serveConfigQuery serveConfigQuery);

}
