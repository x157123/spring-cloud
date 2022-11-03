package com.cloud.sync.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.sync.entity.ServeTable;
import com.cloud.sync.query.ServeTableQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface ServeTableMapper extends BaseMapper<ServeTable> {

    /**
     * 分页查询
     *
     * @param page
     * @param serveTableQuery
     * @return
     */
    IPage<ServeTable> queryPage(IPage<ServeTable> page, @Param("param") ServeTableQuery serveTableQuery);

}
