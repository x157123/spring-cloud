package com.cloud.sync.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.sync.entity.JoinTable;
import com.cloud.sync.query.JoinTableQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface JoinTableMapper extends BaseMapper<JoinTable> {

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @param joinTableQuery 查询条件
     * @return  分页数据
     */
    IPage<JoinTable> queryPage(IPage<JoinTable> page, @Param("param") JoinTableQuery joinTableQuery);

}
