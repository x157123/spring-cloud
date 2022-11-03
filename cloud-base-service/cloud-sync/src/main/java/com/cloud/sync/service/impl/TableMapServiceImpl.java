package com.cloud.sync.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.exceptions.DataException;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.core.utils.DataVersionUtils;
import com.cloud.common.core.utils.ValidationUtils;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.cloud.sync.entity.TableMap;
import com.cloud.sync.vo.TableMapVo;
import com.cloud.sync.mapper.TableMapMapper;
import com.cloud.sync.query.TableMapQuery;
import com.cloud.sync.service.TableMapService;
import com.cloud.sync.param.TableMapParam;
import com.cloud.sync.vo.ServeTableVo;
import com.cloud.sync.service.ServeTableService;
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
public class TableMapServiceImpl implements TableMapService {

    private TableMapMapper tableMapMapper;

    private ServeTableService serveTableService;

    /**
     * 使用构造方法注入
     *
     * @param tableMapMapper
     * @param serveTableService;
     */
    public TableMapServiceImpl(TableMapMapper tableMapMapper, ServeTableService serveTableService){
        this.tableMapMapper = tableMapMapper;
        this.serveTableService=serveTableService;
    }

    /**
     * 保存对象
     *
     * @param tableMapParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(TableMapParam tableMapParam) {
        ValidationUtils.validate(tableMapParam);
        TableMap tableMap = BeanUtil.copyProperties(tableMapParam, TableMap::new);
        if (tableMapParam.getId() != null) {
            return this.updateById(tableMap);
        }
        tableMapMapper.insert(tableMap);
        return Boolean.TRUE;
    }

    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    @Override
    public TableMapVo findById(Long id) {
        List<TableMapVo> tableMapVos = this.findByIds(Collections.singletonList(id));
        if (CollectionUtils.isEmpty(tableMapVos)) {
            return null;
        }
        return CollectionUtils.firstElement(tableMapVos);
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return  返回list结果
     */
    @Override
    public List<TableMapVo> findByIds(List<Long> ids) {
        LambdaQueryWrapper<TableMap> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TableMap::getId, ids);
        List<TableMap> tableMapEntities = tableMapMapper.selectList(queryWrapper);
        //数据转换
        List<TableMapVo> list = BeanUtil.copyListProperties(tableMapEntities, TableMapVo::new);
        //封装关联数据
		this.setParam(list);
		return list;
    }

    /**
     * 根据查询条件 查询列表
     *
     * @param tableMapQuery 查询条件
     * @return 返回list结果
     */
    @Override
    public List<TableMapVo> findByList(TableMapQuery tableMapQuery) {
        IPage<TableMapVo> iPage = this.queryPage(tableMapQuery, new PageParam());
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
        LambdaQueryWrapper<TableMap> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TableMap::getId, ids);
        tableMapMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param tableMapQuery 查询条件
     * @param pageParam 分页条件
     * @return 分页数据
     */
    @Override
    public IPage<TableMapVo> queryPage(TableMapQuery tableMapQuery, PageParam pageParam) {
        IPage<TableMap> iPage = tableMapMapper.queryPage(OrderUtil.getPage(pageParam), tableMapQuery);
        IPage<TableMapVo> page = iPage.convert(tableMap -> BeanUtil.copyProperties(tableMap, TableMapVo::new));
		this.setParam(page.getRecords());
        return page;
    }

    /**
     * 通过Id 更新数据
     *
     * @param tableMap 前端更新集合
     * @return  更新成功状态
     */
    private Boolean updateById(TableMap tableMap) {
        LambdaQueryWrapper<TableMap> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TableMap::getId, tableMap.getId())
                .eq(TableMap::getVersion, tableMap.getVersion());
        tableMap.setVersion(DataVersionUtils.next());
        int count = tableMapMapper.update(tableMap, queryWrapper);
        if (count <= 0) {
            throw new DataException("数据保存异常,未更新到任何数据");
        }
        return Boolean.TRUE;
    }

	
	/**
     * 传入多个Id 查询数据
     *
     * @param writeConnectIds
     * @return
     */
    @Override
    public List<TableMapVo> findByWriteConnectId(List<Long> writeConnectIds){
        if (writeConnectIds == null || writeConnectIds.size() <= 0) {
            return new ArrayList<>();
        }
		LambdaQueryWrapper<TableMap> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TableMap::getWriteConnectId, writeConnectIds);
        List<TableMap> tableMapEntities = tableMapMapper.selectList(queryWrapper);
        //数据转换
        List<TableMapVo> list = BeanUtil.copyListProperties(tableMapEntities, TableMapVo::new);
        return list;
	}
	
	/**
     * 传入多个Id 查询数据
     *
     * @param writeTableIds
     * @return
     */
    @Override
    public List<TableMapVo> findByWriteTableId(List<Long> writeTableIds){
        if (writeTableIds == null || writeTableIds.size() <= 0) {
            return new ArrayList<>();
        }
		LambdaQueryWrapper<TableMap> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TableMap::getWriteTableId, writeTableIds);
        List<TableMap> tableMapEntities = tableMapMapper.selectList(queryWrapper);
        //数据转换
        List<TableMapVo> list = BeanUtil.copyListProperties(tableMapEntities, TableMapVo::new);
        return list;
	}
	
	/**
     * 传入多个Id 查询数据
     *
     * @param readTableIds
     * @return
     */
    @Override
    public List<TableMapVo> findByReadTableId(List<Long> readTableIds){
        if (readTableIds == null || readTableIds.size() <= 0) {
            return new ArrayList<>();
        }
		LambdaQueryWrapper<TableMap> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TableMap::getReadTableId, readTableIds);
        List<TableMap> tableMapEntities = tableMapMapper.selectList(queryWrapper);
        //数据转换
        List<TableMapVo> list = BeanUtil.copyListProperties(tableMapEntities, TableMapVo::new);
        return list;
	}
	
	/**
     * 传入多个Id 查询数据
     *
     * @param readConnectIds
     * @return
     */
    @Override
    public List<TableMapVo> findByReadConnectId(List<Long> readConnectIds){
        if (readConnectIds == null || readConnectIds.size() <= 0) {
            return new ArrayList<>();
        }
		LambdaQueryWrapper<TableMap> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TableMap::getReadConnectId, readConnectIds);
        List<TableMap> tableMapEntities = tableMapMapper.selectList(queryWrapper);
        //数据转换
        List<TableMapVo> list = BeanUtil.copyListProperties(tableMapEntities, TableMapVo::new);
        return list;
	}

	/**
     * 补充关联表数据查询
     *
     * @param list 列表数据
     */
    private void setParam(List<TableMapVo> list) {
        if (list != null) {
            List<Long> ids = list.stream().map(TableMapVo::getId).collect(Collectors.toList());
            Map<Long, List<ServeTableVo>> serveTableMap = serveTableService.findByTableMapId(ids).stream().collect(Collectors.groupingBy(ServeTableVo::getTableMapId));
            for (TableMapVo tableMap : list) {
                tableMap.setServeTableVOList(serveTableMap.get(tableMap.getId()));
            }
        }
    }
}
