package com.cloud.sync.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.sync.entity.TableAssociate;
import com.cloud.sync.query.TableAssociateQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author liulei
 */
@Mapper
public interface TableAssociateMapper extends BaseMapper<TableAssociate> {

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @param tableAssociateQuery 查询条件
     * @return  分页数据
     */
    IPage<TableAssociate> queryPage(IPage<TableAssociate> page, @Param("param") TableAssociateQuery tableAssociateQuery);

}
