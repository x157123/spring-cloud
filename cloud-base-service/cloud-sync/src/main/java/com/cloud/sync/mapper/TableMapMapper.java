package com.cloud.sync.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.sync.entity.TableMap;
import com.cloud.sync.query.TableMapQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface TableMapMapper extends BaseMapper<TableMap> {

    /**
     * 分页查询
     *
     * @param page
     * @param tableMapQuery
     * @return
     */
    IPage<TableMap> queryPage(IPage<TableMap> page, @Param("param") TableMapQuery tableMapQuery);

}
