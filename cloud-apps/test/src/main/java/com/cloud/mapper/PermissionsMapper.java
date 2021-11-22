package com.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.entity.Permissions;
import com.cloud.query.PermissionsQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface PermissionsMapper extends BaseMapper<Permissions> {

    /**
     * 分页查询
     *
     * @param page
     * @param permissionsQuery
     * @return
     */
    IPage<Permissions> queryPage(IPage<Permissions> page, @Param("param") PermissionsQuery permissionsQuery);

}
