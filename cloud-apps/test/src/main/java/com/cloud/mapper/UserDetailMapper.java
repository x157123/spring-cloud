package com.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.entity.UserDetail;
import com.cloud.query.UserDetailQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface UserDetailMapper extends BaseMapper<UserDetail> {

    /**
     * 分页查询
     *
     * @param page
     * @param userDetailQuery
     * @return
     */
    IPage<UserDetail> queryPage(IPage<UserDetail> page, @Param("param") UserDetailQuery userDetailQuery);

}
