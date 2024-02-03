package com.cloud.sync.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.exceptions.DataException;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.core.utils.DataVersionUtils;
import com.cloud.common.core.utils.ValidationUtils;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.cloud.sync.base.service.ColumnConfigService;
import com.cloud.sync.base.service.TableConfigService;
import com.cloud.sync.entity.TableConfig;
import com.cloud.sync.base.mapper.TableConfigMapper;
import com.cloud.sync.param.TableConfigParam;
import com.cloud.sync.query.TableConfigQuery;
import com.cloud.sync.vo.ColumnConfigVo;
import com.cloud.sync.vo.TableConfigVo;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    /**
     * 使用构造方法注入
     *
     * @param tableConfigMapper 同步表配置Mapper服务
     */
    public TableConfigServiceImpl(TableConfigMapper tableConfigMapper, ColumnConfigService columnConfigService) {
        this.tableConfigMapper = tableConfigMapper;
        this.columnConfigService = columnConfigService;
    }

    /**
     * 保存对象
     *
     * @param tableConfigParams 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    @Transactional
    public Boolean save(List<TableConfigParam> tableConfigParams) {
        ValidationUtils.validate(tableConfigParams);
        List<TableConfig> list = BeanUtil.copyListProperties(tableConfigParams, TableConfig::new);
        LambdaQueryWrapper<TableConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TableConfig::getServeId, tableConfigParams.get(0).getServeId());
        tableConfigMapper.delete(queryWrapper);
        for (TableConfig tableConfig : list) {
            tableConfig.setVersion(DataVersionUtils.next());
            tableConfigMapper.insert(tableConfig);
        }
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
     * @return 返回list结果
     */
    @Override
    public List<TableConfigVo> findByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
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
        if (CollectionUtils.isEmpty(ids)) {
            return Boolean.FALSE;
        }
        LambdaQueryWrapper<TableConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TableConfig::getId, ids);
        tableConfigMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param tableConfigQuery 查询条件
     * @param pageParam        分页条件
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
     * @param serveIds id集合
     * @return 返回查询结果
     */
    @Override
    public List<TableConfigVo> findByServeId(List<Long> serveIds, Integer type) {
        if (serveIds == null || serveIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<TableConfigVo> dataList = new ArrayList<>();
        LambdaQueryWrapper<TableConfig> queryWrapper = new LambdaQueryWrapper<>();
        List<List<Long>> subLists = ListUtils.partition(serveIds, 5000);
        for (List<Long> list : subLists) {
            queryWrapper.in(TableConfig::getServeId, list);
            if (type != null) {
                queryWrapper.eq(TableConfig::getType, type);
            }
            dataList.addAll(queryWrapper(queryWrapper));
        }
        setParam(dataList);
        return dataList;
    }

    /**
     * 通过Id 更新数据
     *
     * @param tableConfig 前端更新集合
     * @return 更新成功状态
     */
    private Boolean update(TableConfig tableConfig) {
        LambdaQueryWrapper<TableConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TableConfig::getId, tableConfig.getId())
                .eq(TableConfig::getVersion, tableConfig.getVersion());
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
            for (TableConfigVo tableConfig : list) {
                tableConfig.setColumnConfigVoList(columnConfigMap.get(tableConfig.getId()));
            }
        }
    }

    /**
     * 查询数据列表
     *
     * @param queryWrapper 查询条件
     * @return 返回转化后的数据
     */
    private List<TableConfigVo> queryWrapper(LambdaQueryWrapper<TableConfig> queryWrapper) {
        // 数据查询
        List<TableConfig> tableConfigEntities = tableConfigMapper.selectList(queryWrapper);
        // 数据转换
        return BeanUtil.copyListProperties(tableConfigEntities, TableConfigVo::new);
    }
}