package com.cloud.sync.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.exceptions.DataException;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.core.utils.DataVersionUtils;
import com.cloud.common.core.utils.ValidationUtils;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.cloud.sync.entity.ColumnConfig;
import com.cloud.sync.vo.ColumnConfigVo;
import com.cloud.sync.mapper.ColumnConfigMapper;
import com.cloud.sync.query.ColumnConfigQuery;
import com.cloud.sync.service.ColumnConfigService;
import com.cloud.sync.param.ColumnConfigParam;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author liulei
 */
@Service
public class ColumnConfigServiceImpl implements ColumnConfigService {

    private ColumnConfigMapper columnConfigMapper;

    /**
     * 使用构造方法注入
     *
     * @param columnConfigMapper
     */
    public ColumnConfigServiceImpl(ColumnConfigMapper columnConfigMapper){
        this.columnConfigMapper = columnConfigMapper;
    }

    /**
     * 保存对象
     *
     * @param columnConfigParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(ColumnConfigParam columnConfigParam) {
        ValidationUtils.validate(columnConfigParam);
        ColumnConfig columnConfig = BeanUtil.copyProperties(columnConfigParam, ColumnConfig::new);
        if (columnConfigParam.getId() != null) {
            return this.updateById(columnConfig);
        }
        columnConfigMapper.insert(columnConfig);
        return Boolean.TRUE;
    }

    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    @Override
    public ColumnConfigVo findById(Long id) {
        List<ColumnConfigVo> columnConfigVos = this.findByIds(Collections.singletonList(id));
        if (CollectionUtils.isEmpty(columnConfigVos)) {
            return null;
        }
        return CollectionUtils.firstElement(columnConfigVos);
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return  返回list结果
     */
    @Override
    public List<ColumnConfigVo> findByIds(List<Long> ids) {
        LambdaQueryWrapper<ColumnConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ColumnConfig::getId, ids);
        List<ColumnConfig> columnConfigEntities = columnConfigMapper.selectList(queryWrapper);
        //数据转换
        List<ColumnConfigVo> list = BeanUtil.copyListProperties(columnConfigEntities, ColumnConfigVo::new);
		return list;
    }

    /**
     * 根据查询条件 查询列表
     *
     * @param columnConfigQuery 查询条件
     * @return 返回list结果
     */
    @Override
    public List<ColumnConfigVo> findByList(ColumnConfigQuery columnConfigQuery) {
        IPage<ColumnConfigVo> iPage = this.queryPage(columnConfigQuery, new PageParam());
        return iPage.getRecords();
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids id集合
     * @return 删除情况状态
     */
    @Override
    public Boolean removeByIds(List<Long> ids) {
        LambdaQueryWrapper<ColumnConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ColumnConfig::getId, ids);
        columnConfigMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param columnConfigQuery 查询条件
     * @param pageParam 分页条件
     * @return 分页数据
     */
    @Override
    public IPage<ColumnConfigVo> queryPage(ColumnConfigQuery columnConfigQuery, PageParam pageParam) {
        IPage<ColumnConfig> iPage = columnConfigMapper.queryPage(OrderUtil.getPage(pageParam), columnConfigQuery);
        IPage<ColumnConfigVo> page = iPage.convert(columnConfig -> BeanUtil.copyProperties(columnConfig, ColumnConfigVo::new));
        return page;
    }

    /**
     * 通过Id 更新数据
     *
     * @param columnConfig 前端更新集合
     * @return  更新成功状态
     */
    private Boolean updateById(ColumnConfig columnConfig) {
        LambdaQueryWrapper<ColumnConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ColumnConfig::getId, columnConfig.getId())
                .eq(ColumnConfig::getVersion, columnConfig.getVersion());
        columnConfig.setVersion(DataVersionUtils.next());
        int count = columnConfigMapper.update(columnConfig, queryWrapper);
        if (count <= 0) {
            throw new DataException("数据保存异常,未更新到任何数据");
        }
        return Boolean.TRUE;
    }

	
	/**
     * 传入多个Id 查询数据
     *
     * @param connectIds
     * @return
     */
    @Override
    public List<ColumnConfigVo> findByConnectId(List<Long> connectIds){
        if (connectIds == null || connectIds.size() <= 0) {
            return new ArrayList<>();
        }
		LambdaQueryWrapper<ColumnConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ColumnConfig::getConnectId, connectIds);
        List<ColumnConfig> columnConfigEntities = columnConfigMapper.selectList(queryWrapper);
        //数据转换
        List<ColumnConfigVo> list = BeanUtil.copyListProperties(columnConfigEntities, ColumnConfigVo::new);
        return list;
	}
	
	/**
     * 传入多个Id 查询数据
     *
     * @param tableIds
     * @return
     */
    @Override
    public List<ColumnConfigVo> findByTableId(List<Long> tableIds){
        if (tableIds == null || tableIds.size() <= 0) {
            return new ArrayList<>();
        }
		LambdaQueryWrapper<ColumnConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ColumnConfig::getTableId, tableIds);
        List<ColumnConfig> columnConfigEntities = columnConfigMapper.selectList(queryWrapper);
        //数据转换
        List<ColumnConfigVo> list = BeanUtil.copyListProperties(columnConfigEntities, ColumnConfigVo::new);
        return list;
	}
}
