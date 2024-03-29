package com.cloud.sync.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.sync.entity.TableConfig;
import com.cloud.sync.query.TableConfigQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface TableConfigMapper extends BaseMapper<TableConfig> {

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @param tableConfigQuery 查询条件
     * @return  分页数据
     */
    IPage<TableConfig> queryPage(IPage<TableConfig> page, @Param("param") TableConfigQuery tableConfigQuery);

}
