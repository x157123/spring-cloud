package com.tianque.scgrid.service.issue.focus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tianque.scgrid.service.issue.focus.entity.IssueFocusTag;
import com.tianque.scgrid.service.issue.focus.query.IssueFocusTagQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface IssueFocusTagMapper extends BaseMapper<IssueFocusTag> {

    /**
     * 分页查询
     *
     * @param page
     * @param issueFocusTagQuery
     * @return
     */
    IPage<IssueFocusTag> queryPage(IPage<IssueFocusTag> page, @Param("param") IssueFocusTagQuery issueFocusTagQuery);

}
