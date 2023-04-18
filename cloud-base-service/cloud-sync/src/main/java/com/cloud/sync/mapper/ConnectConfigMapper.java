package com.cloud.sync.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.sync.entity.connectConfig;
import com.cloud.sync.query.connectConfigQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface connectConfigMapper extends BaseMapper<connectConfig> {

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @param connectConfigQuery 查询条件
     * @return  分页数据
     */
    IPage<connectConfig> queryPage(IPage<connectConfig> page, @Param("param") connectConfigQuery connectConfigQuery);

}
