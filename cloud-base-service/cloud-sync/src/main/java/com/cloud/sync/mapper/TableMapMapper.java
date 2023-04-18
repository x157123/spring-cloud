package com.cloud.sync.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.sync.entity.tableMap;
import com.cloud.sync.query.tableMapQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface tableMapMapper extends BaseMapper<tableMap> {

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @param tableMapQuery 查询条件
     * @return  分页数据
     */
    IPage<tableMap> queryPage(IPage<tableMap> page, @Param("param") tableMapQuery tableMapQuery);

}
