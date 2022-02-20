package com.tianque.scgrid.service.issue.link.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tianque.scgrid.service.issue.link.entity.IssueLinkPerson;
import com.tianque.scgrid.service.issue.link.query.IssueLinkPersonQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface IssueLinkPersonMapper extends BaseMapper<IssueLinkPerson> {

    /**
     * 分页查询
     *
     * @param page
     * @param issueLinkPersonQuery
     * @return
     */
    IPage<IssueLinkPerson> queryPage(IPage<IssueLinkPerson> page, @Param("param") IssueLinkPersonQuery issueLinkPersonQuery);

}
