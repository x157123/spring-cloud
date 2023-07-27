package com.cloud.sync.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.sync.entity.Serve;
import com.cloud.sync.query.ServeQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface ServeMapper extends BaseMapper<Serve> {

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @param serveQuery 查询条件
     * @return  分页数据
     */
    IPage<Serve> queryPage(IPage<Serve> page, @Param("param") ServeQuery serveQuery);

}
