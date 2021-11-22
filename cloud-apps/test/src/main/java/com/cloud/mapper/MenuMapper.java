package com.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.entity.Menu;
import com.cloud.query.MenuQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 分页查询
     *
     * @param page
     * @param menuQuery
     * @return
     */
    IPage<Menu> queryPage(IPage<Menu> page, @Param("param") MenuQuery menuQuery);

}
