package com.cloud.sync.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.exceptions.DataException;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.core.utils.DataVersionUtils;
import com.cloud.common.core.utils.ValidationUtils;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.cloud.sync.entity.TableConfig;
import com.cloud.sync.vo.TableConfigVo;
import com.cloud.sync.mapper.TableConfigMapper;
import com.cloud.sync.query.TableConfigQuery;
import com.cloud.sync.service.TableConfigService;
import com.cloud.sync.param.TableConfigParam;
import com.cloud.sync.vo.ColumnConfigVo;
import com.cloud.sync.service.ColumnConfigService;
import com.cloud.sync.vo.TableMapVo;
import com.cloud.sync.service.TableMapService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liulei
 */
@Service
public class TableConfigServiceImpl implements TableConfigService {

    private final TableConfigMapper tableConfigMapper;

    private final ColumnConfigService columnConfigService;

    private final TableMapService tableMapService;

    /**
     * 使用构造方法注入
     *
     * @param tableConfigMapper 同步表配置Mapper服务
     * @param columnConfigService  同步数据库列配置Mapper服务
     * @param tableMapService  表映射Mapper服务
     */
    public TableConfigServiceImpl(TableConfigMapper tableConfigMapper, ColumnConfigService columnConfigService, TableMapService tableMapService){
        this.tableConfigMapper = tableConfigMapper;
        this.columnConfigService = columnConfigService;
        this.tableMapService = tableMapService;
    }

    /**
     * 保存对象
     *
     * @param tableConfigParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(TableConfigParam tableConfigParam) {
        ValidationUtils.validate(tableConfigParam);
        TableConfig tableConfig = BeanUtil.copyProperties(tableConfigParam, TableConfig::new);
        if (tableConfig != null && tableConfig.getId() != null) {
            LambdaQueryWrapper<TableConfig> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TableConfig::getId, tableConfig.getId())
                    .eq(TableConfig::getVersion, tableConfig.getVersion());
            tableConfig.setVersion(DataVersionUtils.next());
            return this.update(queryWrapper, tableConfig);
        }
        tableConfigMapper.insert(tableConfig);
        return Boolean.TRUE;
    }

    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    @Override
    public TableConfigVo findById(Long id) {
        List<TableConfigVo> tableConfigVos = this.findByIds(Collections.singletonList(id));
        if (CollectionUtils.isEmpty(tableConfigVos)) {
            return null;
        }
        return CollectionUtils.firstElement(tableConfigVos);
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return  返回list结果
     */
    @Override
    public List<TableConfigVo> findByIds(List<Long> ids) {
        LambdaQueryWrapper<TableConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TableConfig::getId, ids);
        List<TableConfigVo> list = queryWrapper(queryWrapper);
        //封装关联数据
		this.setParam(list);
		return list;
    }

    /**
     * 根据查询条件 查询列表
     *
     * @param tableConfigQuery 查询条件
     * @return 返回list结果
     */
    @Override
    public List<TableConfigVo> findByList(TableConfigQuery tableConfigQuery) {
        IPage<TableConfigVo> iPage = this.queryPage(tableConfigQuery, new PageParam());
        //封装关联数据
		this.setParam(iPage.getRecords());
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
        LambdaQueryWrapper<TableConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TableConfig::getId, ids);
        tableConfigMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param tableConfigQuery 查询条件
     * @param pageParam 分页条件
     * @return 分页数据
     */
    @Override
    public IPage<TableConfigVo> queryPage(TableConfigQuery tableConfigQuery, PageParam pageParam) {
        IPage<TableConfig> iPage = tableConfigMapper.queryPage(OrderUtil.getPage(pageParam), tableConfigQuery);
        IPage<TableConfigVo> page = iPage.convert(tableConfig -> BeanUtil.copyProperties(tableConfig, TableConfigVo::new));
		this.setParam(page.getRecords());
        return page;
    }
	
	/**
     * 传入多个Id 查询数据
     *
     * @param connectIds id集合
     * @return  返回查询结果
     */
    @Override
    public List<TableConfigVo> findByConnectId(List<Long> connectIds){
        if (connectIds == null || connectIds.size() == 0) {
            return new ArrayList<>();
        }
		LambdaQueryWrapper<TableConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TableConfig::getConnectId, connectIds);
        return queryWrapper(queryWrapper);
	}

    /**
     * 通过Id 更新数据
     *
     * @param tableConfig 前端更新集合
     * @return  更新成功状态
     */
    private Boolean update(LambdaQueryWrapper<TableConfig> queryWrapper, TableConfig tableConfig) {
        tableConfig.setVersion(DataVersionUtils.next());
        int count = tableConfigMapper.update(tableConfig, queryWrapper);
        if (count <= 0) {
            throw new DataException("数据保存异常,未更新到任何数据");
        }
        return Boolean.TRUE;
    }

	/**
     * 补充关联表数据查询
     *
     * @param list 列表数据
     */
    private void setParam(List<TableConfigVo> list) {
        if (list != null) {
            List<Long> ids = list.stream().map(TableConfigVo::getId).collect(Collectors.toList());
            Map<Long, List<ColumnConfigVo>> columnConfigMap = columnConfigService.findByTableId(ids).stream().collect(Collectors.groupingBy(ColumnConfigVo::getTableId));
            Map<Long, List<TableMapVo>> tableMapMap = tableMapService.findByReadTableId(ids).stream().collect(Collectors.groupingBy(TableMapVo::getReadTableId));
            for (TableConfigVo tableConfig : list) {
                tableConfig.setColumnConfigVOList(columnConfigMap.get(tableConfig.getId()));
                tableConfig.setTableMapVOList(tableMapMap.get(tableConfig.getId()));
            }
        }
    }

    /**
     * 查询数据列表
     *
     * @param queryWrapper 查询条件
     * @return  返回转化后的数据
     */
    private List<TableConfigVo> queryWrapper(LambdaQueryWrapper<TableConfig> queryWrapper){
        // 数据查询
        List<TableConfig> tableConfigEntities = tableConfigMapper.selectList(queryWrapper);
        // 数据转换
        return BeanUtil.copyListProperties(tableConfigEntities, TableConfigVo::new);
    }
}
