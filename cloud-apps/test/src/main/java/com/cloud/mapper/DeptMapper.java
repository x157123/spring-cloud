package com.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.entity.Dept;
import com.cloud.query.DeptQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * 分页查询
     *
     * @param page
     * @param deptQuery
     * @return
     */
    IPage<Dept> queryPage(IPage<Dept> page, @Param("param") DeptQuery deptQuery);

}
