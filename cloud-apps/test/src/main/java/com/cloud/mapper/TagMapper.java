package com.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.entity.Tag;
import com.cloud.query.TagQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 分页查询
     *
     * @param page
     * @param tagQuery
     * @return
     */
    IPage<Tag> queryPage(IPage<Tag> page, @Param("param") TagQuery tagQuery);

}
