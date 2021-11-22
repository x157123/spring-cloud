package com.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.entity.User;
import com.cloud.query.UserQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 分页查询
     *
     * @param page
     * @param userQuery
     * @return
     */
    IPage<User> queryPage(IPage<User> page, @Param("param") UserQuery userQuery);

}
