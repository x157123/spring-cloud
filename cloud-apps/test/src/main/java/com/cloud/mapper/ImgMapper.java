package com.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.entity.Img;
import com.cloud.query.ImgQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface ImgMapper extends BaseMapper<Img> {

    /**
     * 分页查询
     *
     * @param page
     * @param imgQuery
     * @return
     */
    IPage<Img> queryPage(IPage<Img> page, @Param("param") ImgQuery imgQuery);

}
